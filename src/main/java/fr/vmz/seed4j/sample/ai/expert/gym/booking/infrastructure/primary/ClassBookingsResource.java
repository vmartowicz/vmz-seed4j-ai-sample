package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.booking.application.ClassBookingsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingId;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.SessionId;
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
@RequestMapping("/api/gym/bookings")
class ClassBookingsResource {

  private final ClassBookingsApplicationService bookings;

  ClassBookingsResource(ClassBookingsApplicationService bookings) {
    this.bookings = bookings;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestClassBooking book(@RequestBody RestClassBookingRequest request) {
    return RestClassBooking.from(bookings.book(request.member(), request.session()));
  }

  @GetMapping("/{bookingId}")
  RestClassBooking booking(@PathVariable UUID bookingId) {
    return RestClassBooking.from(bookings.booking(new ClassBookingId(bookingId)));
  }

  @GetMapping
  List<RestClassBooking> roster(@RequestParam UUID sessionId) {
    return bookings.roster(new SessionId(sessionId)).stream().map(RestClassBooking::from).toList();
  }

  @PostMapping("/{bookingId}/cancellation")
  RestClassBooking cancel(@PathVariable UUID bookingId) {
    return RestClassBooking.from(bookings.cancel(new ClassBookingId(bookingId)));
  }

  @PostMapping("/{bookingId}/no-show")
  RestClassBooking markNoShow(@PathVariable UUID bookingId) {
    return RestClassBooking.from(bookings.markNoShow(new ClassBookingId(bookingId)));
  }
}
