package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class ClassBookingTest {

  @Test
  void shouldBookSeatWhenClassIsNotFull() {
    var member = MemberId.newId();
    var session = session(10);

    var booking = booked(member, session);

    assertThat(booking.id()).isNotNull();
    assertThat(booking.member()).isEqualTo(member);
    assertThat(booking.session()).isEqualTo(session);
    assertThat(booking.requestedAt()).isEqualTo(NOW);
    assertThat(booking.status()).isEqualTo(ClassBookingStatus.BOOKED);
    assertThat(booking.holdsSeat()).isTrue();
    assertThat(booking.isActive()).isTrue();
    assertThat(booking.countsInQuota()).isTrue();
  }

  @Test
  void shouldWaitWhenClassIsFull() {
    var booking = waiting();

    assertThat(booking.status()).isEqualTo(ClassBookingStatus.WAITING);
    assertThat(booking.holdsSeat()).isFalse();
    assertThat(booking.isActive()).isTrue();
    assertThat(booking.countsInQuota()).isFalse();
  }

  @Nested
  class Cancel {

    @Test
    void shouldCancelForFreeUntil2HoursBefore() {
      var booking = booked();

      assertThat(booking.cancel(START.minusHours(2))).isTrue();

      assertThat(booking.status()).isEqualTo(ClassBookingStatus.CANCELLED);
      assertThat(booking.countsInQuota()).isFalse();
      assertThat(booking.isNoShow()).isFalse();
    }

    @Test
    void shouldCountLateCancellationAsNoShow() {
      var booking = booked();

      assertThat(booking.cancel(START.minusHours(2).plusMinutes(1))).isTrue();

      assertThat(booking.status()).isEqualTo(ClassBookingStatus.LATE_CANCELLED);
      assertThat(booking.countsInQuota()).isTrue();
      assertThat(booking.isNoShow()).isTrue();
    }

    @Test
    void shouldLeaveWaitingListWithoutFreeingSeat() {
      var booking = waiting();

      assertThat(booking.cancel(START.minusMinutes(10))).isFalse();

      assertThat(booking.status()).isEqualTo(ClassBookingStatus.CANCELLED);
    }

    @Test
    void shouldNotCancelOnceClassStarted() {
      var booking = booked();

      assertThatThrownBy(() -> booking.cancel(START)).isExactlyInstanceOf(ClassAlreadyStartedException.class);
    }

    @Test
    void shouldNotCancelTwice() {
      var booking = booked();
      booking.cancel(NOW);

      assertThatThrownBy(() -> booking.cancel(NOW))
        .isExactlyInstanceOf(InvalidClassBookingTransitionException.class)
        .hasMessageContaining("CANCELLED");
    }
  }

  @Nested
  class NoShow {

    @Test
    void shouldMarkNoShowOnceClassStarted() {
      var booking = booked();

      booking.markNoShow(START);

      assertThat(booking.status()).isEqualTo(ClassBookingStatus.NO_SHOW);
      assertThat(booking.isNoShow()).isTrue();
      assertThat(booking.countsInQuota()).isTrue();
      assertThat(booking.isActive()).isFalse();
    }

    @Test
    void shouldNotMarkNoShowBeforeClass() {
      var booking = booked();

      assertThatThrownBy(() -> booking.markNoShow(START.minusMinutes(1))).isExactlyInstanceOf(ClassNotStartedException.class);
    }

    @Test
    void shouldNotMarkWaitingBookingAsNoShow() {
      var booking = waiting();

      assertThatThrownBy(() -> booking.markNoShow(START)).isExactlyInstanceOf(InvalidClassBookingTransitionException.class);
    }
  }

  @Test
  void shouldPromoteWaitingBooking() {
    var booking = waiting();

    booking.promote();

    assertThat(booking.status()).isEqualTo(ClassBookingStatus.BOOKED);
  }

  @Test
  void shouldNotPromoteBookedSeat() {
    var booking = booked();

    assertThatThrownBy(booking::promote).isExactlyInstanceOf(InvalidClassBookingTransitionException.class);
  }

  @Test
  void shouldBeCancelledByGymWithoutCountingInQuota() {
    var seat = booked();
    var waiting = waiting();
    var cancelled = booked();
    cancelled.cancel(NOW);

    assertThat(seat.cancelByGym()).isTrue();
    assertThat(waiting.cancelByGym()).isTrue();
    assertThat(cancelled.cancelByGym()).isFalse();

    assertThat(seat.status()).isEqualTo(ClassBookingStatus.CANCELLED_BY_GYM);
    assertThat(seat.countsInQuota()).isFalse();
    assertThat(cancelled.status()).isEqualTo(ClassBookingStatus.CANCELLED);
  }
}
