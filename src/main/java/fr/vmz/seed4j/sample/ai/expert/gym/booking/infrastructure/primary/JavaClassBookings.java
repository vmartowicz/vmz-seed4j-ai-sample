package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.booking.application.ClassBookingsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.SessionId;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaClassBookings {

  private final ClassBookingsApplicationService bookings;

  public JavaClassBookings(ClassBookingsApplicationService bookings) {
    this.bookings = bookings;
  }

  public void sessionCancelled(UUID session) {
    bookings.sessionCancelled(new SessionId(session));
  }
}
