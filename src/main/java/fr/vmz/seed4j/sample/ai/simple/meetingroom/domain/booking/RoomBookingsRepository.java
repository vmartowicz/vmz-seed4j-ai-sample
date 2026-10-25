package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import java.util.List;
import java.util.Optional;

public interface RoomBookingsRepository {
  void save(RoomBooking booking);

  Optional<RoomBooking> get(RoomBookingId booking);

  List<RoomBooking> activeForRoom(RoomId room);

  List<RoomBooking> activeForEmployee(EmployeeEmail employee);

  default RoomBooking booking(RoomBookingId booking) {
    return get(booking).orElseThrow(() -> new UnknownRoomBookingException(booking));
  }
}
