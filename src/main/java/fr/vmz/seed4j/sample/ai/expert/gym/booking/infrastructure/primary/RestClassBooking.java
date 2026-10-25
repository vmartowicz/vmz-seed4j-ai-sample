package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBooking;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingStatus;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.LocalDateTime;
import java.util.UUID;

record RestClassBooking(UUID id, UUID memberId, UUID sessionId, Gym gym, LocalDateTime start, ClassBookingStatus status, LocalDateTime requestedAt) {
  static RestClassBooking from(ClassBooking booking) {
    var session = booking.session();

    return new RestClassBooking(
      booking.id().get(),
      booking.member().get(),
      session.id().get(),
      session.gym(),
      session.start(),
      booking.status(),
      booking.requestedAt()
    );
  }
}
