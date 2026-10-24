package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.Booking;
import java.time.LocalDateTime;
import java.util.UUID;

record RestBooking(UUID id, UUID serviceId, LocalDateTime start, LocalDateTime end, String customerName, String phone, String status) {
  static RestBooking from(Booking booking) {
    return new RestBooking(
      booking.id().get(),
      booking.service().get(),
      booking.slot().start(),
      booking.slot().end(),
      booking.customer().name().get(),
      booking.customer().phone().get(),
      booking.status().name()
    );
  }
}
