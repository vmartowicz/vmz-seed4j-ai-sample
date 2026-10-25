package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record RoomBookingToCreate(RoomId room, EmployeeEmail employee, HourSlot slot) {
  public RoomBookingToCreate {
    Assert.notNull("room", room);
    Assert.notNull("employee", employee);
    Assert.notNull("slot", slot);
  }

  public RoomBooking create() {
    return new RoomBooking(RoomBookingId.newId(), this);
  }
}
