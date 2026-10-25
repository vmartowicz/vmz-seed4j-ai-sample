package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;

public final class RoomBooking {

  private final RoomBookingId id;
  private final RoomId room;
  private final EmployeeEmail employee;
  private final HourSlot slot;
  private RoomBookingStatus status = RoomBookingStatus.ACTIVE;

  RoomBooking(RoomBookingId id, RoomBookingToCreate booking) {
    this.id = id;
    room = booking.room();
    employee = booking.employee();
    slot = booking.slot();
  }

  public void cancel(EmployeeEmail by, LocalDateTime now) {
    Assert.notNull("by", by);
    Assert.notNull("now", now);

    if (status == RoomBookingStatus.CANCELLED) {
      throw new RoomBookingAlreadyCancelledException(id);
    }

    if (!by.equals(employee)) {
      throw new NotTheBookerException(by);
    }

    if (!now.isBefore(slot.start())) {
      throw new RoomBookingStartedException(id);
    }

    status = RoomBookingStatus.CANCELLED;
  }

  public boolean isCurrentAt(LocalDateTime now) {
    return status == RoomBookingStatus.ACTIVE && now.isBefore(slot.end());
  }

  public RoomBookingId id() {
    return id;
  }

  public RoomId room() {
    return room;
  }

  public EmployeeEmail employee() {
    return employee;
  }

  public HourSlot slot() {
    return slot;
  }

  public RoomBookingStatus status() {
    return status;
  }
}
