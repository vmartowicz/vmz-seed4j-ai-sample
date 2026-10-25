package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.secondary.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.EmployeeEmail;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBooking;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBookingId;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBookingStatus;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBookingsRepository;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryRoomBookingsRepository implements RoomBookingsRepository {

  private final Map<RoomBookingId, RoomBooking> bookings = new ConcurrentHashMap<>();

  @Override
  public void save(RoomBooking booking) {
    bookings.put(booking.id(), booking);
  }

  @Override
  public Optional<RoomBooking> get(RoomBookingId booking) {
    return Optional.ofNullable(bookings.get(booking));
  }

  @Override
  public List<RoomBooking> activeForRoom(RoomId room) {
    return active(booking -> booking.room().equals(room));
  }

  @Override
  public List<RoomBooking> activeForEmployee(EmployeeEmail employee) {
    return active(booking -> booking.employee().equals(employee));
  }

  private List<RoomBooking> active(Predicate<RoomBooking> filter) {
    return bookings
      .values()
      .stream()
      .filter(booking -> booking.status() == RoomBookingStatus.ACTIVE)
      .filter(filter)
      .toList();
  }
}
