package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import java.time.LocalDateTime;

public final class RoomBookingsFixture {

  // Tuesday 2026-03-10 at 10:00, in Paris
  public static final LocalDateTime NOW = LocalDateTime.of(2026, 3, 10, 10, 0);
  public static final LocalDateTime TOMORROW_10_AM = NOW.plusDays(1);

  private RoomBookingsFixture() {}

  public static EmployeeEmail jane() {
    return new EmployeeEmail("jane.doe@corp.com");
  }

  public static RoomBooking booking(RoomId room, LocalDateTime start) {
    return booking(room, jane(), start);
  }

  public static RoomBooking booking(RoomId room, EmployeeEmail employee, LocalDateTime start) {
    return new RoomBookingToCreate(room, employee, new HourSlot(start)).create();
  }
}
