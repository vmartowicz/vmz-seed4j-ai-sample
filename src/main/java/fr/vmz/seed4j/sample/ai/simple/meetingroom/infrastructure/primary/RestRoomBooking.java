package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBooking;
import java.time.LocalDateTime;
import java.util.UUID;

record RestRoomBooking(UUID id, UUID roomId, LocalDateTime start, LocalDateTime end, String employeeEmail, String status) {
  static RestRoomBooking from(RoomBooking booking) {
    return new RestRoomBooking(
      booking.id().get(),
      booking.room().get(),
      booking.slot().start(),
      booking.slot().end(),
      booking.employee().get(),
      booking.status().name()
    );
  }
}
