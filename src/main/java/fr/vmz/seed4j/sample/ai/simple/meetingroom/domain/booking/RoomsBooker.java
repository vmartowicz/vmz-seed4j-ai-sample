package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomsRepository;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class RoomsBooker {

  private static final Comparator<RoomBooking> BY_START = Comparator.comparing(booking -> booking.slot().start());

  private final RoomsRepository rooms;
  private final RoomBookingsRepository bookings;

  public RoomsBooker(RoomsRepository rooms, RoomBookingsRepository bookings) {
    this.rooms = rooms;
    this.bookings = bookings;
  }

  public RoomBooking book(RoomId roomId, EmployeeEmail employee, HourSlot slot, LocalDateTime now) {
    Assert.notNull("slot", slot);
    Assert.notNull("now", now);

    var room = rooms.room(roomId);
    if (!slot.start().isAfter(now)) {
      throw new SlotAlreadyStartedException(slot);
    }

    if (bookings.activeForRoom(room.id()).stream().anyMatch(booking -> booking.slot().equals(slot))) {
      throw new RoomAlreadyBookedException(slot);
    }

    var booking = new RoomBookingToCreate(room.id(), employee, slot).create();
    bookings.save(booking);

    return booking;
  }

  public RoomBooking cancel(RoomBookingId bookingId, EmployeeEmail by, LocalDateTime now) {
    var booking = bookings.booking(bookingId);
    booking.cancel(by, now);
    bookings.save(booking);

    return booking;
  }

  public List<RoomBooking> currentBookingsOf(EmployeeEmail employee, LocalDateTime now) {
    return bookings
      .activeForEmployee(employee)
      .stream()
      .filter(booking -> booking.isCurrentAt(now))
      .sorted(BY_START)
      .toList();
  }
}
