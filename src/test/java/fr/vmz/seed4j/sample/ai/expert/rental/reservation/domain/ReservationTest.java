package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import static fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import org.junit.jupiter.api.Test;

@UnitTest
class ReservationTest {

  @Test
  void shouldConfirmPaidReservation() {
    var reservation = confirmed();

    assertThat(reservation.id()).isNotNull();
    assertThat(reservation.driver()).isEqualTo(jane());
    assertThat(reservation.category()).isEqualTo(Category.ECO);
    assertThat(reservation.period()).isEqualTo(threeDays());
    assertThat(reservation.options()).isEqualTo(Options.NONE);
    assertThat(reservation.quote().total()).isEqualByComparingTo("105");
    assertThat(reservation.freeDay()).isFalse();
    assertThat(reservation.isYoungDriver()).isFalse();
    assertThat(reservation.status()).isEqualTo(ReservationStatus.CONFIRMED);
    assertThat(reservation.refund()).isEmpty();
  }

  @Test
  void shouldPriceYoungDriver() {
    var reservation = eco(young()).book(false, NOW);

    assertThat(reservation.isYoungDriver()).isTrue();
    assertThat(reservation.quote().youngDriverFee()).isEqualByComparingTo("45");
  }

  @Test
  void shouldRefundEverythingWhenCancellingMoreThan48HoursBefore() {
    var reservation = confirmed();

    reservation.cancel(PICK_UP.minusHours(48).minusMinutes(1));

    assertThat(reservation.status()).isEqualTo(ReservationStatus.CANCELLED);
    assertThat(reservation.refund()).hasValueSatisfying(refund -> assertThat(refund).isEqualByComparingTo("105"));
  }

  @Test
  void shouldKeepOneDayWhenCancellingLate() {
    var reservation = confirmed();

    reservation.cancel(PICK_UP.minusHours(48));

    assertThat(reservation.refund()).hasValueSatisfying(refund -> assertThat(refund).isEqualByComparingTo("70"));
  }

  @Test
  void shouldNotRefundMoreThanPaid() {
    var reservation = new ReservationToBook(jane(), Category.ECO, new RentalPeriod(threeDays().pickUpPoint(), PICK_UP, threeDays().returnPoint(), PICK_UP.plusHours(5)), Options.NONE, true).book(false, NOW);

    reservation.cancel(PICK_UP.minusHours(1));

    assertThat(reservation.refund()).hasValueSatisfying(refund -> assertThat(refund).isEqualByComparingTo("0"));
  }

  @Test
  void shouldNotCancelTwice() {
    var reservation = confirmed();
    reservation.cancel(NOW);

    assertThatThrownBy(() -> reservation.cancel(NOW)).isExactlyInstanceOf(InvalidReservationTransitionException.class).hasMessageContaining(
      "CANCELLED"
    );
  }

  @Test
  void shouldBePickedUpOnce() {
    var reservation = confirmed();

    reservation.markPickedUp();

    assertThat(reservation.status()).isEqualTo(ReservationStatus.PICKED_UP);
    assertThatThrownBy(reservation::markPickedUp).isExactlyInstanceOf(InvalidReservationTransitionException.class);
    assertThatThrownBy(() -> reservation.cancel(NOW)).isExactlyInstanceOf(InvalidReservationTransitionException.class);
  }
}
