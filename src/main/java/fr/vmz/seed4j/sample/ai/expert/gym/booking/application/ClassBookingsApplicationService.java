package fr.vmz.seed4j.sample.ai.expert.gym.booking.application;

import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.BookableSessions;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBooking;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingDesk;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingId;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingsRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.MemberNotifications;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.MemberPlans;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.SessionId;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClassBookingsApplicationService {

  private final ClassBookingDesk desk;
  private final Clock clock;

  public ClassBookingsApplicationService(
    ClassBookingsRepository bookings,
    BookableSessions sessions,
    MemberPlans plans,
    MemberNotifications notifications,
    Clock clock
  ) {
    desk = new ClassBookingDesk(bookings, sessions, plans, notifications);
    this.clock = clock;
  }

  public ClassBooking book(MemberId member, SessionId session) {
    return desk.book(member, session, now());
  }

  public ClassBooking booking(ClassBookingId booking) {
    return desk.booking(booking);
  }

  public List<ClassBooking> roster(SessionId session) {
    return desk.roster(session);
  }

  public ClassBooking cancel(ClassBookingId booking) {
    return desk.cancel(booking, now());
  }

  public ClassBooking markNoShow(ClassBookingId booking) {
    return desk.markNoShow(booking, now());
  }

  public void sessionCancelled(SessionId session) {
    desk.sessionCancelled(session);
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
