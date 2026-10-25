package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import static fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingsFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.TablesFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.BlacklistedDinerException;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Contact;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Diner;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinerEmail;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinersRepository;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.UnknownDinerException;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class BookingDeskTest {

  private final DinersRepository diners = mock(DinersRepository.class, CALLS_REAL_METHODS);
  private final TableBookingsRepository bookings = mock(TableBookingsRepository.class, CALLS_REAL_METHODS);
  private final BookingDesk desk = new BookingDesk(diners, bookings);

  private Diner jane;

  @BeforeEach
  void loadDiner() {
    jane = Diner.newcomer(jane().email());
    doReturn(Optional.of(jane)).when(diners).get(jane().email());
    doReturn(List.of()).when(bookings).on(TUESDAY);
  }

  @Nested
  class Book {

    @Test
    void shouldBookNewcomerAtWindowTable() {
      doReturn(Optional.empty()).when(diners).get(jane().email());

      var booking = desk.book(bookingFor(2), NOW);

      assertThat(booking.status()).isEqualTo(BookingStatus.CONFIRMED);
      assertThat(booking.tables()).containsExactly(table(1));
      verify(diners).save(argThat(diner -> diner.email().equals(jane().email())));
      verify(bookings).save(booking);
    }

    @Test
    void shouldBookFreeTable() {
      doReturn(List.of(confirmedBooking(), cancelledBookingAt(2))).when(bookings).on(TUESDAY);

      assertThat(desk.book(bookingFor(2), NOW).tables()).containsExactly(table(2));
    }

    @Test
    void shouldAskDepositFor6People() {
      var booking = desk.book(bookingFor(6), NOW);

      assertThat(booking.status()).isEqualTo(BookingStatus.PENDING);
      assertThat(booking.deposit()).isPresent();
    }

    @Test
    void shouldGiveWindowAndNoDepositToLoyalDiners() {
      IntStream.range(0, 5).forEach(visit -> jane.honor());

      var booking = desk.book(bookingFor(4), NOW);

      assertThat(booking.tables()).containsExactly(table(11));
      assertThat(desk.book(bookingFor(6), NOW).status()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void shouldNotBookForBlacklistedDiner() {
      IntStream.range(0, 3).forEach(visit -> jane.noShow());

      assertThatThrownBy(() -> desk.book(bookingFor(2), NOW)).isExactlyInstanceOf(BlacklistedDinerException.class);
      verify(bookings, never()).save(any());
    }

    @Test
    void shouldNotBookWhenRestaurantIsClosed() {
      var sunday = new BookingToCreate(jane(), new Slot(TUESDAY.plusDays(5), LocalTime.NOON), new PartySize(2), null);

      assertThatThrownBy(() -> desk.book(sunday, NOW)).isExactlyInstanceOf(RestaurantClosedException.class);
    }

    @Test
    void shouldNotBookWithoutFreeTable() {
      doReturn(List.of(bookingFor(5).create(tables(17), false, NOW), bookingFor(5).create(tables(18), false, NOW))).when(bookings).on(TUESDAY);

      assertThatThrownBy(() -> desk.book(bookingFor(5), NOW)).isExactlyInstanceOf(NoTableAvailableException.class).hasMessageContaining(
        "5 people"
      );
      verify(bookings, never()).save(any());
    }
  }

  @Nested
  class Follow {

    @Test
    void shouldPayDeposit() {
      var booking = known(pendingBooking());

      desk.payDeposit(booking.id(), new BigDecimal("120"), NOW);

      assertThat(booking.status()).isEqualTo(BookingStatus.CONFIRMED);
      verify(bookings).save(booking);
    }

    @Test
    void shouldCountCancellationOfDiner() {
      var booking = known(confirmedBooking());

      desk.cancel(booking.id(), NOW);

      assertThat(booking.status()).isEqualTo(BookingStatus.CANCELLED);
      assertThat(jane.cancellationsIn(Year.of(2026))).isEqualTo(1);
      verify(bookings).save(booking);
      verify(diners).save(jane);
    }

    @Test
    void shouldCountHonoredBookingOfDiner() {
      var booking = known(confirmedBooking());

      desk.checkIn(booking.id(), DINNER_TIME);

      assertThat(booking.status()).isEqualTo(BookingStatus.HONORED);
      assertThat(jane.honoredBookings()).isEqualTo(1);
      verify(diners).save(jane);
    }

    @Test
    void shouldCountNoShowOfDiner() {
      var booking = known(confirmedBooking());

      desk.noShow(booking.id(), DINNER_TIME);

      assertThat(booking.status()).isEqualTo(BookingStatus.NO_SHOW);
      assertThat(jane.noShows()).isEqualTo(1);
      verify(diners).save(jane);
    }

    @Test
    void shouldNotFollowUnknownBooking() {
      var unknown = TableBookingId.newId();
      doReturn(Optional.empty()).when(bookings).get(unknown);

      assertThatThrownBy(() -> desk.cancel(unknown, NOW)).isExactlyInstanceOf(UnknownTableBookingException.class);
    }

    @Test
    void shouldCancelOnlyExpiredUnpaidBookings() {
      var expired = pendingBooking();
      var paid = paidBooking();
      doReturn(List.of(expired, paid)).when(bookings).pending();

      desk.expireUnpaid(NOW.plusHours(49));

      assertThat(expired.status()).isEqualTo(BookingStatus.CANCELLED);
      assertThat(jane.cancellationsIn(Year.of(2026))).isZero();
      verify(bookings).save(expired);
      verify(bookings, never()).save(paid);
    }
  }

  @Nested
  class Availability {

    @Test
    void shouldListArrivalTimesWithProposedTables() {
      var slots = desk.availability(TUESDAY, MealService.DINNER, new PartySize(2), NOW);

      assertThat(slots).hasSize(6).allSatisfy(slot -> assertThat(slot.tables()).containsExactly(table(1)));
      assertThat(slots.getFirst().arrival()).isEqualTo(LocalTime.of(19, 0));
    }

    @Test
    void shouldNotListSlotsOfFullService() {
      doReturn(List.of(bookingFor(5).create(tables(17), false, NOW), bookingFor(5).create(tables(18), false, NOW))).when(bookings).on(TUESDAY);

      assertThat(desk.availability(TUESDAY, MealService.DINNER, new PartySize(6), NOW)).isEmpty();
    }

    @Test
    void shouldNotListSlotsTooLateToBook() {
      assertThat(desk.availability(TUESDAY, MealService.LUNCH, new PartySize(2), TUESDAY.atTime(11, 0))).isEmpty();
    }
  }

  @Nested
  class DayBookings {

    @Test
    void shouldListBookingsOfServiceByArrivalTimeThenName() {
      var late = bookingAt("20:30", 1);
      var early = bookingAt("19:00", 2);
      var albert = new BookingToCreate(
        new Contact("Albert Martin", new DinerEmail("albert@mail.com"), jane().phone()),
        new Slot(TUESDAY, LocalTime.of(19, 0)),
        new PartySize(2),
        null
      ).create(tables(5), false, NOW);
      var lunch = new BookingToCreate(jane(), new Slot(TUESDAY, LocalTime.NOON), new PartySize(2), null).create(tables(3), false, NOW);
      doReturn(List.of(late, cancelledBookingAt(4), lunch, early, albert)).when(bookings).on(TUESDAY);

      assertThat(desk.dayBookings(TUESDAY, MealService.DINNER)).containsExactly(albert, early, late);
    }
  }

  @Nested
  class Diners {

    @Test
    void shouldGetKnownDiner() {
      assertThat(desk.diner(jane().email())).isSameAs(jane);
    }

    @Test
    void shouldNotGetUnknownDiner() {
      var unknown = new DinerEmail("unknown@mail.com");
      doReturn(Optional.empty()).when(diners).get(unknown);

      assertThatThrownBy(() -> desk.diner(unknown)).isExactlyInstanceOf(UnknownDinerException.class).hasMessageContaining(
        "unknown@mail.com"
      );
    }
  }

  private TableBooking known(TableBooking booking) {
    doReturn(Optional.of(booking)).when(bookings).get(booking.id());

    return booking;
  }

  private static TableBooking bookingAt(String time, int table) {
    return new BookingToCreate(jane(), new Slot(TUESDAY, LocalTime.parse(time)), new PartySize(2), null).create(tables(table), false, NOW);
  }

  private static TableBooking cancelledBookingAt(int table) {
    var booking = bookingAt("19:30", table);
    booking.cancel(NOW);

    return booking;
  }
}
