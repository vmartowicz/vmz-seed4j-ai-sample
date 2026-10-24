package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import static fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingsFixture.*;
import static fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.CatalogFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class BookingTest {

  @Test
  void shouldCreateConfirmedBookingWithoutReminder() {
    var booking = massageTomorrowAt10();

    assertThat(booking.id()).isNotNull();
    assertThat(booking.service()).isEqualTo(MASSAGE.id());
    assertThat(booking.customer()).isEqualTo(customer());
    assertThat(booking.slot()).isEqualTo(massageSlotAt(TOMORROW_10_AM));
    assertThat(booking.status()).isEqualTo(BookingStatus.CONFIRMED);
    assertThat(booking.reminderSent()).isFalse();
  }

  @Nested
  class ChangeSlot {

    @Test
    void shouldChangeSlotAndResetReminder() {
      var booking = massageTomorrowAt10();
      booking.markReminderSent();

      booking.changeSlot(massageSlotAt(TOMORROW_10_AM.plusHours(2)), NOW);

      assertThat(booking.slot()).isEqualTo(massageSlotAt(TOMORROW_10_AM.plusHours(2)));
      assertThat(booking.reminderSent()).isFalse();
    }

    @Test
    void shouldChangeSlotOneMinuteBeforeStart() {
      var booking = massageTomorrowAt10();

      booking.changeSlot(massageSlotAt(TOMORROW_10_AM.plusHours(2)), TOMORROW_10_AM.minusMinutes(1));

      assertThat(booking.slot().start()).isEqualTo(TOMORROW_10_AM.plusHours(2));
    }

    @Test
    void shouldNotChangeSlotOnceStarted() {
      var booking = massageTomorrowAt10();

      assertThatThrownBy(() -> booking.changeSlot(massageSlotAt(TOMORROW_10_AM.plusHours(2)), TOMORROW_10_AM)).isExactlyInstanceOf(
        BookingStartedException.class
      );
      assertThat(booking.slot().start()).isEqualTo(TOMORROW_10_AM);
    }

    @Test
    void shouldNotChangeSlotOfCancelledBooking() {
      var booking = massageTomorrowAt10();
      booking.cancel(NOW);

      assertThatThrownBy(() -> booking.changeSlot(massageSlotAt(TOMORROW_10_AM.plusHours(2)), NOW))
        .isExactlyInstanceOf(BookingCancelledException.class)
        .hasMessageContaining(booking.id().get().toString());
    }
  }

  @Nested
  class Cancel {

    @Test
    void shouldCancelBooking() {
      var booking = massageTomorrowAt10();

      booking.cancel(NOW);

      assertThat(booking.status()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void shouldNotCancelOnceStarted() {
      var booking = massageTomorrowAt10();

      assertThatThrownBy(() -> booking.cancel(TOMORROW_10_AM)).isExactlyInstanceOf(BookingStartedException.class);
      assertThat(booking.status()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void shouldNotCancelCancelledBooking() {
      var booking = massageTomorrowAt10();
      booking.cancel(NOW);

      assertThatThrownBy(() -> booking.cancel(NOW)).isExactlyInstanceOf(BookingCancelledException.class);
    }
  }

  @Nested
  class Reminder {

    @Test
    void shouldNeedReminderForConfirmedBookingOfTheDay() {
      assertThat(massageTomorrowAt10().needsReminderOn(TOMORROW)).isTrue();
    }

    @Test
    void shouldNotNeedReminderForAnotherDay() {
      assertThat(massageTomorrowAt10().needsReminderOn(TOMORROW.plusDays(1))).isFalse();
    }

    @Test
    void shouldNotNeedReminderTwice() {
      var booking = massageTomorrowAt10();
      booking.markReminderSent();

      assertThat(booking.needsReminderOn(TOMORROW)).isFalse();
    }

    @Test
    void shouldNotNeedReminderForCancelledBooking() {
      var booking = massageTomorrowAt10();
      booking.cancel(NOW);

      assertThat(booking.needsReminderOn(TOMORROW)).isFalse();
    }
  }
}
