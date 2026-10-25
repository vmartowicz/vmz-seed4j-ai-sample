package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.secondary.schedule;

import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.BookableSession;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.BookableSessions;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.SessionId;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary.JavaClassSessions;
import java.util.Optional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class ScheduleBookableSessions implements BookableSessions {

  private final JavaClassSessions sessions;

  // Lazy: the schedule tells bookings about cancelled sessions, the contexts depend on each other
  ScheduleBookableSessions(@Lazy JavaClassSessions sessions) {
    this.sessions = sessions;
  }

  @Override
  public Optional<BookableSession> session(SessionId session) {
    return sessions
      .session(session.get())
      .map(found -> new BookableSession(session, found.gym(), found.start(), found.capacity(), found.cancelled()));
  }
}
