package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import static fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@UnitTest
class ReservationDeskTest {

  private final ReservationsRepository reservations = mock(ReservationsRepository.class, CALLS_REAL_METHODS);
  private final LoyaltyCard loyalty = mock(LoyaltyCard.class);
  private final ReservationDesk desk = new ReservationDesk(reservations, loyalty);

  @Test
  void shouldBookAndPayReservation() {
    var reservation = desk.book(eco(jane()), NOW);

    assertThat(reservation.status()).isEqualTo(ReservationStatus.CONFIRMED);
    verify(reservations).save(reservation);
  }

  @Test
  void shouldOfferAdditionalDriverToGoldMembers() {
    doReturn(true).when(loyalty).isGold(jane().email());

    var reservation = desk.book(new ReservationToBook(jane(), Category.ECO, threeDays(), new Options(false, true, false), false), NOW);

    assertThat(reservation.quote().options()).isEqualByComparingTo("0");
  }

  @Test
  void shouldNotBookPickUpInThePast() {
    var past = new ReservationToBook(jane(), Category.ECO, threeDays(), Options.NONE, false);

    assertThatThrownBy(() -> desk.book(past, PICK_UP)).isExactlyInstanceOf(PickUpInThePastException.class);
  }

  @Test
  void shouldNotBookForIneligibleDriver() {
    var child = new Driver("Kid", jane().email(), PICK_UP.toLocalDate().minusYears(18), PICK_UP.toLocalDate().minusYears(3));

    assertThatThrownBy(() -> desk.book(eco(child), NOW)).isExactlyInstanceOf(DriverNotEligibleException.class);
    verify(reservations, never()).save(any());
  }

  @ParameterizedTest
  @EnumSource(value = Category.class, names = { "PREMIUM", "VAN" })
  void shouldNotRentPremiumOrVanToYoungDrivers(Category category) {
    var booking = new ReservationToBook(young(), category, threeDays(), Options.NONE, false);

    assertThatThrownBy(() -> desk.book(booking, NOW)).isExactlyInstanceOf(YoungDriverCategoryException.class);
  }

  @Test
  void shouldRedeemPointsForFreeDay() {
    doReturn(true).when(loyalty).redeemFreeDay(eq(jane().email()), any());

    var reservation = desk.book(new ReservationToBook(jane(), Category.COMFORT, threeDays(), Options.NONE, true), NOW);

    assertThat(reservation.quote().freeDay()).isEqualByComparingTo("49");
    verify(loyalty).redeemFreeDay(jane().email(), reservation.id().get());
  }

  @Test
  void shouldNotOfferFreeDayWithoutEnoughPoints() {
    var booking = new ReservationToBook(jane(), Category.ECO, threeDays(), Options.NONE, true);

    assertThatThrownBy(() -> desk.book(booking, NOW)).isExactlyInstanceOf(NotEnoughLoyaltyPointsException.class);
    verify(reservations, never()).save(any());
  }

  @ParameterizedTest
  @EnumSource(value = Category.class, names = { "SUV", "VAN", "PREMIUM" })
  void shouldOfferFreeDaysOnlyInEcoOrComfort(Category category) {
    var booking = new ReservationToBook(jane(), category, threeDays(), Options.NONE, true);

    assertThatThrownBy(() -> desk.book(booking, NOW)).isExactlyInstanceOf(FreeDayCategoryException.class);
    verifyNoInteractions(loyalty);
  }

  @Test
  void shouldGiveBackPointsOfCancelledFreeDay() {
    var reservation = known(new ReservationToBook(jane(), Category.ECO, threeDays(), Options.NONE, true).book(false, NOW));

    desk.cancel(reservation.id(), NOW);

    verify(loyalty).cancelRedemption(jane().email(), reservation.id().get());
    verify(reservations).save(reservation);
  }

  @Test
  void shouldCancelWithoutPoints() {
    var reservation = known(confirmed());

    desk.cancel(reservation.id(), NOW);

    assertThat(reservation.status()).isEqualTo(ReservationStatus.CANCELLED);
    verifyNoInteractions(loyalty);
  }

  @Test
  void shouldMarkReservationAsPickedUp() {
    var reservation = known(confirmed());

    desk.markPickedUp(reservation.id());

    assertThat(desk.reservation(reservation.id()).status()).isEqualTo(ReservationStatus.PICKED_UP);
  }

  @Test
  void shouldNotFindUnknownReservation() {
    var unknown = ReservationId.newId();
    doReturn(Optional.empty()).when(reservations).get(unknown);

    assertThatThrownBy(() -> desk.cancel(unknown, NOW)).isExactlyInstanceOf(UnknownReservationException.class);
  }

  private Reservation known(Reservation reservation) {
    doReturn(Optional.of(reservation)).when(reservations).get(reservation.id());

    return reservation;
  }
}
