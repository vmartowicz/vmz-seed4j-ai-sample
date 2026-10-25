package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class ClassBookingDeskTest {

  private final ClassBookingsRepository bookings = mock(ClassBookingsRepository.class, CALLS_REAL_METHODS);
  private final BookableSessions sessions = mock(BookableSessions.class);
  private final MemberPlans plans = mock(MemberPlans.class);
  private final MemberNotifications notifications = mock(MemberNotifications.class);
  private final ClassBookingDesk desk = new ClassBookingDesk(bookings, sessions, plans, notifications);

  private final MemberId member = MemberId.newId();
  private final List<ClassBooking> memberBookings = new ArrayList<>();
  private final List<ClassBooking> sessionBookings = new ArrayList<>();
  private BookableSession session;

  @BeforeEach
  void loadSession() {
    session = known(session(2));
    plan(member, true, false, Gym.CONFLUENCE);
    doReturn(memberBookings).when(bookings).of(member);
    doReturn(sessionBookings).when(bookings).ofSession(session.id());
  }

  @Nested
  class Book {

    @Test
    void shouldBookSeat() {
      var booking = desk.book(member, session.id(), NOW);

      assertThat(booking.status()).isEqualTo(ClassBookingStatus.BOOKED);
      verify(bookings).save(booking);
    }

    @Test
    void shouldJoinWaitingListOfFullClass() {
      sessionBookings.addAll(List.of(booked(MemberId.newId(), session), booked(MemberId.newId(), session), waitingFor(MemberId.newId())));

      assertThat(desk.book(member, session.id(), NOW).status()).isEqualTo(ClassBookingStatus.WAITING);
    }

    @Test
    void shouldBookUpTo7DaysBefore() {
      assertThat(desk.book(member, session.id(), START.minusDays(7)).status()).isEqualTo(ClassBookingStatus.BOOKED);
    }

    @Test
    void shouldNotBookMoreThan7DaysBefore() {
      assertThatThrownBy(() -> desk.book(member, session.id(), START.minusDays(7).minusMinutes(1))).isExactlyInstanceOf(
        BookingNotOpenException.class
      );
    }

    @Test
    void shouldBookUntil1HourBefore() {
      assertThat(desk.book(member, session.id(), START.minusHours(1)).status()).isEqualTo(ClassBookingStatus.BOOKED);
    }

    @Test
    void shouldNotBookLessThan1HourBefore() {
      assertThatThrownBy(() -> desk.book(member, session.id(), START.minusMinutes(59))).isExactlyInstanceOf(BookingClosedException.class);
    }

    @Test
    void shouldNotBookCancelledSession() {
      var cancelled = known(new BookableSession(SessionId.newId(), Gym.CONFLUENCE, START, 10, true));

      assertThatThrownBy(() -> desk.book(member, cancelled.id(), NOW)).isExactlyInstanceOf(SessionCancelledByGymException.class);
    }

    @Test
    void shouldNotBookUnknownSession() {
      var unknown = SessionId.newId();
      doReturn(Optional.empty()).when(sessions).session(unknown);

      assertThatThrownBy(() -> desk.book(member, unknown, NOW)).isExactlyInstanceOf(UnknownBookableSessionException.class);
    }

    @Test
    void shouldNotBookForUnknownMember() {
      var unknown = MemberId.newId();
      doReturn(Optional.empty()).when(plans).planOf(unknown);

      assertThatThrownBy(() -> desk.book(unknown, session.id(), NOW)).isExactlyInstanceOf(UnknownClassBookingMemberException.class);
    }

    @Test
    void shouldNotBookForInactiveMember() {
      plan(member, false, true, Gym.CONFLUENCE);

      assertNotEligible(Ineligibility.MEMBERSHIP_INACTIVE);
    }

    @Test
    void shouldNotBookOutsideHomeGymWithEssentialPlan() {
      plan(member, true, false, Gym.ECULLY);

      assertNotEligible(Ineligibility.OTHER_GYM);
    }

    @Test
    void shouldBookAnyGymWithPremiumPlan() {
      plan(member, true, true, Gym.ECULLY);
      IntStream.range(0, 4).forEach(booking -> memberBookings.add(booked(member, session(10))));

      assertThat(desk.book(member, session.id(), NOW).status()).isEqualTo(ClassBookingStatus.BOOKED);
    }

    @Test
    void shouldNotBookWhenBanned() {
      memberBookings.addAll(List.of(noShowAt(member, NOW.minusDays(3)), noShowAt(member, NOW.minusDays(2)), noShowAt(member, NOW.minusDays(1))));

      assertNotEligible(Ineligibility.BANNED);
    }

    @Test
    void shouldNotBookMoreThan4ClassesAMonthWithEssentialPlan() {
      IntStream.range(0, 4).forEach(booking -> memberBookings.add(booked(member, session(10))));

      assertNotEligible(Ineligibility.QUOTA_REACHED);
    }

    @Test
    void shouldNotBookSameSessionTwice() {
      memberBookings.add(booked(member, session));

      assertThatThrownBy(() -> desk.book(member, session.id(), NOW)).isExactlyInstanceOf(AlreadyBookedException.class);
    }

    @Test
    void shouldBookAgainAfterCancellation() {
      var cancelled = booked(member, session);
      cancelled.cancel(NOW);
      memberBookings.add(cancelled);

      assertThat(desk.book(member, session.id(), NOW).status()).isEqualTo(ClassBookingStatus.BOOKED);
    }

    private void assertNotEligible(Ineligibility reason) {
      assertThatThrownBy(() -> desk.book(member, session.id(), NOW))
        .isExactlyInstanceOf(NotEligibleException.class)
        .hasMessageContaining(reason.explanation());
      verify(bookings, never()).save(any());
    }
  }

  @Nested
  class Cancel {

    @Test
    void shouldPromoteFirstEligibleWaitingMember() {
      var seat = known(booked(member, session));
      var ineligible = MemberId.newId();
      plan(ineligible, false, false, Gym.CONFLUENCE);
      var next = MemberId.newId();
      plan(next, true, false, Gym.CONFLUENCE);
      var unknown = MemberId.newId();
      doReturn(Optional.empty()).when(plans).planOf(unknown);
      var last = MemberId.newId();
      plan(last, true, true, Gym.ECULLY);
      sessionBookings.addAll(List.of(seat, waitingFor(unknown, 1), waitingFor(ineligible, 2), waitingFor(next, 3), waitingFor(last, 4)));

      desk.cancel(seat.id(), NOW);

      assertThat(seat.status()).isEqualTo(ClassBookingStatus.CANCELLED);
      assertThat(statusOf(next)).isEqualTo(ClassBookingStatus.BOOKED);
      assertThat(statusOf(ineligible)).isEqualTo(ClassBookingStatus.WAITING);
      assertThat(statusOf(last)).isEqualTo(ClassBookingStatus.WAITING);
      verify(notifications).notify(eq(next), contains("A spot opened up"));
      verify(notifications, times(1)).notify(any(), anyString());
    }

    @Test
    void shouldNotPromoteLessThan2HoursBeforeClass() {
      var seat = known(booked(member, session));
      var next = MemberId.newId();
      plan(next, true, false, Gym.CONFLUENCE);
      sessionBookings.addAll(List.of(seat, waitingFor(next, 1)));

      desk.cancel(seat.id(), START.minusMinutes(90));

      assertThat(statusOf(next)).isEqualTo(ClassBookingStatus.WAITING);
    }

    @Test
    void shouldNotPromoteWhenWaitingMemberLeaves() {
      var leaving = known(waitingFor(member, 1));
      var next = MemberId.newId();
      plan(next, true, false, Gym.CONFLUENCE);
      sessionBookings.addAll(List.of(leaving, waitingFor(next, 2)));

      desk.cancel(leaving.id(), NOW);

      assertThat(statusOf(next)).isEqualTo(ClassBookingStatus.WAITING);
      verify(bookings).save(leaving);
    }

    @Test
    void shouldNotCancelUnknownBooking() {
      var unknown = ClassBookingId.newId();
      doReturn(Optional.empty()).when(bookings).get(unknown);

      assertThatThrownBy(() -> desk.cancel(unknown, NOW)).isExactlyInstanceOf(UnknownClassBookingException.class);
    }
  }

  @Test
  void shouldMarkNoShow() {
    var seat = known(booked(member, session));

    desk.markNoShow(seat.id(), START);

    assertThat(seat.status()).isEqualTo(ClassBookingStatus.NO_SHOW);
    verify(bookings).save(seat);
  }

  @Test
  void shouldCancelAndNotifyBookingsOfCancelledSession() {
    var seat = booked(member, session);
    var other = MemberId.newId();
    var waiting = waitingFor(other, 1);
    var cancelled = booked(MemberId.newId(), session);
    cancelled.cancel(NOW);
    sessionBookings.addAll(List.of(seat, waiting, cancelled));

    desk.sessionCancelled(session.id());

    assertThat(seat.status()).isEqualTo(ClassBookingStatus.CANCELLED_BY_GYM);
    assertThat(waiting.status()).isEqualTo(ClassBookingStatus.CANCELLED_BY_GYM);
    verify(notifications).notify(eq(member), contains("cancelled by the gym"));
    verify(notifications).notify(eq(other), contains("cancelled by the gym"));
    verify(bookings, never()).save(cancelled);
  }

  @Test
  void shouldListRosterInRequestOrder() {
    var late = waitingFor(MemberId.newId(), 2);
    var early = booked(member, session);
    sessionBookings.addAll(List.of(late, early));

    assertThat(desk.roster(session.id())).containsExactly(early, late);
  }

  private ClassBookingStatus statusOf(MemberId waitingMember) {
    return sessionBookings
      .stream()
      .filter(booking -> booking.member().equals(waitingMember))
      .findFirst()
      .orElseThrow()
      .status();
  }

  private ClassBooking waitingFor(MemberId waitingMember) {
    return waitingFor(waitingMember, 1);
  }

  private ClassBooking waitingFor(MemberId waitingMember, int minutesAfterNow) {
    return ClassBooking.request(waitingMember, session, NOW.plusMinutes(minutesAfterNow), true);
  }

  private ClassBooking known(ClassBooking booking) {
    doReturn(Optional.of(booking)).when(bookings).get(booking.id());

    return booking;
  }

  private BookableSession known(BookableSession bookable) {
    doReturn(Optional.of(bookable)).when(sessions).session(bookable.id());

    return bookable;
  }

  private void plan(MemberId planMember, boolean active, boolean unlimited, Gym homeGym) {
    doReturn(Optional.of(new MemberPlan(active, unlimited, homeGym))).when(plans).planOf(planMember);
    doReturn(List.of()).when(bookings).of(planMember);
    if (planMember.equals(member)) {
      doReturn(memberBookings).when(bookings).of(member);
    }
  }
}
