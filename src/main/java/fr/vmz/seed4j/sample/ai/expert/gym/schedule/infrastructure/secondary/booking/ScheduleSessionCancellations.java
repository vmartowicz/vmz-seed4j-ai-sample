package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.secondary.booking;

import fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.primary.JavaClassBookings;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.SessionCancellations;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.SessionId;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class ScheduleSessionCancellations implements SessionCancellations {

  private final JavaClassBookings bookings;

  // Lazy: bookings read sessions back, the contexts depend on each other
  ScheduleSessionCancellations(@Lazy JavaClassBookings bookings) {
    this.bookings = bookings;
  }

  @Override
  public void cancelled(SessionId session) {
    bookings.sessionCancelled(session.get());
  }
}
