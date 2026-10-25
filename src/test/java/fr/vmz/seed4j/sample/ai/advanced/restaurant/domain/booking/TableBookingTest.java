package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import static fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingsFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.TablesFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class TableBookingTest {

  private static final BigDecimal DEPOSIT = new BigDecimal("120");

  @Nested
  class Create {

    @Test
    void shouldConfirmBookingWithoutDeposit() {
      var booking = confirmedBooking();

      assertThat(booking.id()).isNotNull();
      assertThat(booking.contact()).isEqualTo(jane());
      assertThat(booking.slot()).isEqualTo(dinner());
      assertThat(booking.party()).isEqualTo(new PartySize(2));
      assertThat(booking.comments()).contains("Peanut allergy");
      assertThat(booking.tables()).containsExactly(table(1));
      assertThat(booking.status()).isEqualTo(BookingStatus.CONFIRMED);
      assertThat(booking.deposit()).isEmpty();
    }

    @Test
    void shouldWaitForDepositOf6People() {
      var booking = pendingBooking();

      assertThat(booking.status()).isEqualTo(BookingStatus.PENDING);
      assertThat(booking.deposit()).contains(Deposit.expected(new PartySize(6), NOW, DINNER_TIME));
    }

    @Test
    void shouldConfirm6PeopleOfLoyalDinerWithoutDeposit() {
      var booking = bookingFor(6).create(tables(17), true, NOW);

      assertThat(booking.status()).isEqualTo(BookingStatus.CONFIRMED);
      assertThat(booking.deposit()).isEmpty();
    }

    @Test
    void shouldIgnoreMissingOrBlankComments() {
      assertThat(new BookingToCreate(jane(), dinner(), new PartySize(2), null).create(tables(1), false, NOW).comments()).isEmpty();
      assertThat(new BookingToCreate(jane(), dinner(), new PartySize(2), "  ").create(tables(1), false, NOW).comments()).isEmpty();
    }

    @Test
    void shouldNotAcceptTooLongComments() {
      assertThatThrownBy(() -> new BookingToCreate(jane(), dinner(), new PartySize(2), "a".repeat(501))).isInstanceOf(
        AssertionException.class
      );
    }
  }

  @Nested
  class PayDeposit {

    @Test
    void shouldConfirmBookingOncePaid() {
      var booking = pendingBooking();

      booking.payDeposit(new BigDecimal("120.00"), NOW.plusHours(48));

      assertThat(booking.status()).isEqualTo(BookingStatus.CONFIRMED);
      assertThat(booking.deposit().orElseThrow().status()).isEqualTo(DepositStatus.PAID);
    }

    @Test
    void shouldNotAcceptWrongAmount() {
      var booking = pendingBooking();

      assertThatThrownBy(() -> booking.payDeposit(new BigDecimal("100"), NOW))
        .isExactlyInstanceOf(WrongDepositAmountException.class)
        .hasMessageContaining("120");
      assertThat(booking.status()).isEqualTo(BookingStatus.PENDING);
    }

    @Test
    void shouldNotAcceptPaymentAfterDeadline() {
      var booking = pendingBooking();

      assertThatThrownBy(() -> booking.payDeposit(DEPOSIT, NOW.plusHours(48).plusMinutes(1))).isExactlyInstanceOf(
        DepositDeadlinePassedException.class
      );
    }

    @Test
    void shouldNotPayDepositOfConfirmedBooking() {
      var booking = confirmedBooking();

      assertThatThrownBy(() -> booking.payDeposit(DEPOSIT, NOW))
        .isExactlyInstanceOf(InvalidBookingTransitionException.class)
        .hasMessageContaining("CONFIRMED");
    }

    @Test
    void shouldNotPayWithoutAmount() {
      var booking = pendingBooking();

      assertThatThrownBy(() -> booking.payDeposit(null, NOW)).isInstanceOf(AssertionException.class);
    }
  }

  @Nested
  class Cancel {

    @Test
    void shouldCancelBookingWithoutDeposit() {
      var booking = confirmedBooking();

      booking.cancel(DINNER_TIME.minusHours(1));

      assertThat(booking.status()).isEqualTo(BookingStatus.CANCELLED);
      assertThat(booking.deposit()).isEmpty();
    }

    @Test
    void shouldCancelBookingWithUnpaidDeposit() {
      var booking = pendingBooking();

      booking.cancel(NOW);

      assertThat(booking.status()).isEqualTo(BookingStatus.CANCELLED);
      assertThat(booking.deposit().orElseThrow().status()).isEqualTo(DepositStatus.EXPECTED);
    }

    @Test
    void shouldRefundDepositWhenCancellingMoreThan48HoursBefore() {
      var booking = paidBooking();

      booking.cancel(DINNER_TIME.minusHours(48).minusMinutes(1));

      assertThat(booking.deposit().orElseThrow().status()).isEqualTo(DepositStatus.REFUNDED);
    }

    @Test
    void shouldForfeitDepositWhenCancelling48HoursBefore() {
      var booking = paidBooking();

      booking.cancel(DINNER_TIME.minusHours(48));

      assertThat(booking.deposit().orElseThrow().status()).isEqualTo(DepositStatus.FORFEITED);
    }

    @Test
    void shouldNotCancelTwice() {
      var booking = confirmedBooking();
      booking.cancel(NOW);

      assertThatThrownBy(() -> booking.cancel(NOW)).isExactlyInstanceOf(InvalidBookingTransitionException.class).hasMessageContaining(
        "CANCELLED"
      );
    }
  }

  @Nested
  class CheckIn {

    @Test
    void shouldHonorBookingOnItsDay() {
      var booking = confirmedBooking();

      booking.honor(TUESDAY.atTime(19, 0));

      assertThat(booking.status()).isEqualTo(BookingStatus.HONORED);
    }

    @Test
    void shouldNotHonorBookingBeforeItsDay() {
      var booking = confirmedBooking();

      assertThatThrownBy(() -> booking.honor(NOW)).isExactlyInstanceOf(NotBookingDayException.class);
    }

    @Test
    void shouldNotHonorPendingBooking() {
      var booking = pendingBooking();

      assertThatThrownBy(() -> booking.honor(DINNER_TIME)).isExactlyInstanceOf(InvalidBookingTransitionException.class);
    }
  }

  @Nested
  class NoShow {

    @Test
    void shouldForfeitDepositOfNoShow() {
      var booking = paidBooking();

      booking.markNoShow(DINNER_TIME);

      assertThat(booking.status()).isEqualTo(BookingStatus.NO_SHOW);
      assertThat(booking.deposit().orElseThrow().status()).isEqualTo(DepositStatus.FORFEITED);
    }

    @Test
    void shouldMarkNoShowWithoutDeposit() {
      var booking = confirmedBooking();

      booking.markNoShow(DINNER_TIME.plusHours(1));

      assertThat(booking.status()).isEqualTo(BookingStatus.NO_SHOW);
      assertThat(booking.deposit()).isEmpty();
    }

    @Test
    void shouldNotMarkNoShowBeforeArrivalTime() {
      var booking = confirmedBooking();

      assertThatThrownBy(() -> booking.markNoShow(DINNER_TIME.minusMinutes(1))).isExactlyInstanceOf(NoShowTooEarlyException.class);
    }

    @Test
    void shouldNotMarkNoShowAfterBookingDay() {
      var booking = confirmedBooking();

      assertThatThrownBy(() -> booking.markNoShow(DINNER_TIME.plusDays(1))).isExactlyInstanceOf(NotBookingDayException.class);
    }

    @Test
    void shouldNotMarkHonoredBookingAsNoShow() {
      var booking = confirmedBooking();
      booking.honor(DINNER_TIME);

      assertThatThrownBy(() -> booking.markNoShow(DINNER_TIME)).isExactlyInstanceOf(InvalidBookingTransitionException.class);
    }
  }

  @Nested
  class Expiration {

    @Test
    void shouldNotExpireConfirmedBooking() {
      var booking = confirmedBooking();

      assertThat(booking.expireIfUnpaid(DINNER_TIME)).isFalse();
      assertThat(booking.status()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void shouldNotExpirePendingBookingBeforeDeadline() {
      var booking = pendingBooking();

      assertThat(booking.expireIfUnpaid(NOW.plusHours(48))).isFalse();
      assertThat(booking.status()).isEqualTo(BookingStatus.PENDING);
    }

    @Test
    void shouldCancelPendingBookingAfterDeadline() {
      var booking = pendingBooking();

      assertThat(booking.expireIfUnpaid(NOW.plusHours(48).plusMinutes(1))).isTrue();
      assertThat(booking.status()).isEqualTo(BookingStatus.CANCELLED);
    }
  }

  @Nested
  class Tables {

    @Test
    void shouldHoldTablesOfItsService() {
      assertThat(confirmedBooking().holdsTablesFor(TUESDAY, MealService.DINNER)).isTrue();
      assertThat(pendingBooking().holdsTablesFor(TUESDAY, MealService.DINNER)).isTrue();
    }

    @Test
    void shouldHoldTablesOfHonoredBooking() {
      var booking = confirmedBooking();
      booking.honor(DINNER_TIME);

      assertThat(booking.holdsTablesFor(TUESDAY, MealService.DINNER)).isTrue();
    }

    @Test
    void shouldNotHoldTablesOfOtherServices() {
      assertThat(confirmedBooking().holdsTablesFor(TUESDAY, MealService.LUNCH)).isFalse();
      assertThat(confirmedBooking().holdsTablesFor(TUESDAY.plusDays(1), MealService.DINNER)).isFalse();
    }

    @Test
    void shouldReleaseTablesOfCancelledOrMissedBookings() {
      var cancelled = confirmedBooking();
      cancelled.cancel(NOW);
      var missed = confirmedBooking();
      missed.markNoShow(DINNER_TIME);

      assertThat(cancelled.holdsTablesFor(TUESDAY, MealService.DINNER)).isFalse();
      assertThat(missed.holdsTablesFor(TUESDAY, MealService.DINNER)).isFalse();
    }
  }
}
