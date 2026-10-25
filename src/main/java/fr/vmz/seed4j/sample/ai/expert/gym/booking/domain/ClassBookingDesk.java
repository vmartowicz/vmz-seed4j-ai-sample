package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ClassBookingDesk {

  private static final int BOOKING_OPENS_DAYS = 7;
  private static final int BOOKING_CLOSES_HOURS = 1;
  private static final int PROMOTION_STOPS_HOURS = 2;
  private static final int ESSENTIAL_MONTHLY_CLASSES = 4;
  private static final Comparator<ClassBooking> BY_REQUEST = Comparator.comparing(ClassBooking::requestedAt);

  private final ClassBookingsRepository bookings;
  private final BookableSessions sessions;
  private final MemberPlans plans;
  private final MemberNotifications notifications;

  public ClassBookingDesk(ClassBookingsRepository bookings, BookableSessions sessions, MemberPlans plans, MemberNotifications notifications) {
    this.bookings = bookings;
    this.sessions = sessions;
    this.plans = plans;
    this.notifications = notifications;
  }

  public ClassBooking book(MemberId member, SessionId sessionId, LocalDateTime now) {
    var session = sessions.session(sessionId).orElseThrow(() -> new UnknownBookableSessionException(sessionId));
    if (session.cancelled()) {
      throw new SessionCancelledByGymException(sessionId);
    }

    if (now.isBefore(session.start().minusDays(BOOKING_OPENS_DAYS))) {
      throw new BookingNotOpenException(session);
    }

    if (now.isAfter(session.start().minusHours(BOOKING_CLOSES_HOURS))) {
      throw new BookingClosedException(session);
    }

    var plan = plans.planOf(member).orElseThrow(() -> new UnknownClassBookingMemberException(member));
    var ineligibility = ineligibility(member, plan, session, now);
    if (ineligibility.isPresent()) {
      throw new NotEligibleException(member, ineligibility.orElseThrow());
    }

    if (bookings.of(member).stream().anyMatch(booking -> booking.isActive() && booking.session().id().equals(sessionId))) {
      throw new AlreadyBookedException(member, sessionId);
    }

    var seats = bookings.ofSession(sessionId).stream().filter(ClassBooking::holdsSeat).count();
    var booking = ClassBooking.request(member, session, now, seats >= session.capacity());
    bookings.save(booking);

    return booking;
  }

  public ClassBooking cancel(ClassBookingId id, LocalDateTime now) {
    var booking = bookings.booking(id);

    var seatFreed = booking.cancel(now);
    bookings.save(booking);
    if (seatFreed && now.isBefore(booking.session().start().minusHours(PROMOTION_STOPS_HOURS))) {
      promoteFirstEligible(booking.session(), now);
    }

    return booking;
  }

  public ClassBooking markNoShow(ClassBookingId id, LocalDateTime now) {
    var booking = bookings.booking(id);

    booking.markNoShow(now);
    bookings.save(booking);

    return booking;
  }

  public void sessionCancelled(SessionId session) {
    bookings
      .ofSession(session)
      .stream()
      .filter(ClassBooking::cancelByGym)
      .forEach(booking -> {
        bookings.save(booking);
        notifications.notify(booking.member(), "Your class of " + booking.session().start() + " was cancelled by the gym");
      });
  }

  public ClassBooking booking(ClassBookingId booking) {
    return bookings.booking(booking);
  }

  public List<ClassBooking> roster(SessionId session) {
    return bookings.ofSession(session).stream().sorted(BY_REQUEST).toList();
  }

  private void promoteFirstEligible(BookableSession session, LocalDateTime now) {
    bookings
      .ofSession(session.id())
      .stream()
      .filter(booking -> booking.status() == ClassBookingStatus.WAITING)
      .sorted(BY_REQUEST)
      .filter(booking -> isEligible(booking.member(), session, now))
      .findFirst()
      .ifPresent(booking -> {
        booking.promote();
        bookings.save(booking);
        notifications.notify(booking.member(), "A spot opened up: you are booked for the class of " + session.start());
      });
  }

  private boolean isEligible(MemberId member, BookableSession session, LocalDateTime now) {
    return plans
      .planOf(member)
      .map(plan -> ineligibility(member, plan, session, now).isEmpty())
      .orElse(false);
  }

  private Optional<Ineligibility> ineligibility(MemberId member, MemberPlan plan, BookableSession session, LocalDateTime now) {
    if (!plan.active()) {
      return Optional.of(Ineligibility.MEMBERSHIP_INACTIVE);
    }

    if (!plan.unlimitedClasses() && session.gym() != plan.homeGym()) {
      return Optional.of(Ineligibility.OTHER_GYM);
    }

    var record = new MemberBookingRecord(bookings.of(member));
    if (record.isBannedOn(now.toLocalDate())) {
      return Optional.of(Ineligibility.BANNED);
    }

    if (!plan.unlimitedClasses() && record.bookedIn(YearMonth.from(session.start())) >= ESSENTIAL_MONTHLY_CLASSES) {
      return Optional.of(Ineligibility.QUOTA_REACHED);
    }

    return Optional.empty();
  }
}
