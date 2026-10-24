package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.booking.application.BookingsApplicationService;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingId;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking")
class BookingsResource {

  private final BookingsApplicationService bookings;

  BookingsResource(BookingsApplicationService bookings) {
    this.bookings = bookings;
  }

  @GetMapping("/services")
  List<RestBookableService> services() {
    return bookings.services().stream().map(RestBookableService::from).toList();
  }

  @PostMapping("/bookings")
  @ResponseStatus(HttpStatus.CREATED)
  RestBooking book(@RequestBody RestBookingToCreate booking) {
    return RestBooking.from(bookings.book(booking.service(), booking.customer(), booking.start()));
  }

  @GetMapping("/bookings/{bookingId}")
  RestBooking get(@PathVariable UUID bookingId) {
    return RestBooking.from(bookings.get(new BookingId(bookingId)));
  }

  @PutMapping("/bookings/{bookingId}/slot")
  RestBooking changeSlot(@PathVariable UUID bookingId, @RequestBody RestSlotChange change) {
    return RestBooking.from(bookings.changeSlot(new BookingId(bookingId), change.start()));
  }

  @PostMapping("/bookings/{bookingId}/cancellation")
  RestBooking cancel(@PathVariable UUID bookingId) {
    return RestBooking.from(bookings.cancel(new BookingId(bookingId)));
  }
}
