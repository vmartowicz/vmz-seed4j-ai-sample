package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.application.RoomBookingsApplicationService;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.EmployeeEmail;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBookingId;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meeting-rooms/bookings")
class RoomBookingsResource {

  private final RoomBookingsApplicationService bookings;

  RoomBookingsResource(RoomBookingsApplicationService bookings) {
    this.bookings = bookings;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestRoomBooking book(@RequestBody RestRoomBookingToCreate booking) {
    return RestRoomBooking.from(bookings.book(booking.room(), booking.employee(), booking.slot()));
  }

  @PostMapping("/{bookingId}/cancellation")
  RestRoomBooking cancel(@PathVariable UUID bookingId, @RequestBody RestEmployee employee) {
    return RestRoomBooking.from(bookings.cancel(new RoomBookingId(bookingId), employee.toDomain()));
  }

  @GetMapping
  List<RestRoomBooking> currentBookings(@RequestParam String employee) {
    return bookings.currentBookingsOf(new EmployeeEmail(employee)).stream().map(RestRoomBooking::from).toList();
  }
}
