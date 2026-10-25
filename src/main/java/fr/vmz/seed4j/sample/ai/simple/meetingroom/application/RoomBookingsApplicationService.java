package fr.vmz.seed4j.sample.ai.simple.meetingroom.application;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.EmployeeEmail;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.HourSlot;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBooking;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBookingId;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBookingsRepository;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomsBooker;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomsRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomBookingsApplicationService {

  private final RoomsBooker booker;
  private final Clock clock;

  public RoomBookingsApplicationService(RoomsRepository rooms, RoomBookingsRepository bookings, Clock clock) {
    booker = new RoomsBooker(rooms, bookings);
    this.clock = clock;
  }

  public RoomBooking book(RoomId room, EmployeeEmail employee, HourSlot slot) {
    return booker.book(room, employee, slot, now());
  }

  public RoomBooking cancel(RoomBookingId booking, EmployeeEmail by) {
    return booker.cancel(booking, by, now());
  }

  public List<RoomBooking> currentBookingsOf(EmployeeEmail employee) {
    return booker.currentBookingsOf(employee, now());
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
