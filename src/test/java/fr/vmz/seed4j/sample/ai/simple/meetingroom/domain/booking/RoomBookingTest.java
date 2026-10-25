package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import static fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBookingsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class RoomBookingTest {

  private static final RoomId EVEREST = RoomId.newId();

  @Test
  void shouldCreateActiveBooking() {
    var booking = booking(EVEREST, TOMORROW_10_AM);

    assertThat(booking.id()).isNotNull();
    assertThat(booking.room()).isEqualTo(EVEREST);
    assertThat(booking.employee()).isEqualTo(jane());
    assertThat(booking.slot()).isEqualTo(new HourSlot(TOMORROW_10_AM));
    assertThat(booking.status()).isEqualTo(RoomBookingStatus.ACTIVE);
  }

  @Nested
  class Cancel {

    @Test
    void shouldBeCancelledByItsBookerOneMinuteBeforeStart() {
      var booking = booking(EVEREST, TOMORROW_10_AM);

      booking.cancel(new EmployeeEmail("JANE.DOE@corp.com"), TOMORROW_10_AM.minusMinutes(1));

      assertThat(booking.status()).isEqualTo(RoomBookingStatus.CANCELLED);
    }

    @Test
    void shouldNotBeCancelledByAnotherEmployee() {
      var booking = booking(EVEREST, TOMORROW_10_AM);

      assertThatThrownBy(() -> booking.cancel(new EmployeeEmail("john@corp.com"), NOW))
        .isExactlyInstanceOf(NotTheBookerException.class)
        .hasMessageContaining("john@corp.com");
      assertThat(booking.status()).isEqualTo(RoomBookingStatus.ACTIVE);
    }

    @Test
    void shouldNotBeCancelledOnceStarted() {
      var booking = booking(EVEREST, TOMORROW_10_AM);

      assertThatThrownBy(() -> booking.cancel(jane(), TOMORROW_10_AM)).isExactlyInstanceOf(RoomBookingStartedException.class);
    }

    @Test
    void shouldNotBeCancelledTwice() {
      var booking = booking(EVEREST, TOMORROW_10_AM);
      booking.cancel(jane(), NOW);

      assertThatThrownBy(() -> booking.cancel(jane(), NOW))
        .isExactlyInstanceOf(RoomBookingAlreadyCancelledException.class)
        .hasMessageContaining(booking.id().get().toString());
    }
  }

  @Nested
  class Current {

    @Test
    void shouldBeCurrentBeforeItsStart() {
      assertThat(booking(EVEREST, TOMORROW_10_AM).isCurrentAt(NOW)).isTrue();
    }

    @Test
    void shouldBeCurrentWhileItTakesPlace() {
      assertThat(booking(EVEREST, NOW).isCurrentAt(NOW.plusMinutes(59))).isTrue();
    }

    @Test
    void shouldNotBeCurrentOnceEnded() {
      assertThat(booking(EVEREST, NOW).isCurrentAt(NOW.plusHours(1))).isFalse();
    }

    @Test
    void shouldNotBeCurrentOnceCancelled() {
      var booking = booking(EVEREST, TOMORROW_10_AM);
      booking.cancel(jane(), NOW);

      assertThat(booking.isCurrentAt(NOW)).isFalse();
    }
  }
}
