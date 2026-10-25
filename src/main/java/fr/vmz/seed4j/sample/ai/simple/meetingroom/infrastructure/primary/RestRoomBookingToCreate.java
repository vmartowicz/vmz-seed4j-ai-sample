package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.EmployeeEmail;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.HourSlot;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestRoomBookingToCreate(@Nullable UUID roomId, @Nullable LocalDateTime start, @Nullable String employeeEmail) {
  RoomId room() {
    return new RoomId(roomId);
  }

  HourSlot slot() {
    return new HourSlot(start);
  }

  EmployeeEmail employee() {
    return new EmployeeEmail(employeeEmail);
  }
}
