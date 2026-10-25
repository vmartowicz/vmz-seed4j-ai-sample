package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.application.TableBookingsApplicationService;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.MealService;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingId;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import java.time.LocalDate;
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
@RequestMapping("/api/restaurant")
class TableBookingsResource {

  private final TableBookingsApplicationService bookings;

  TableBookingsResource(TableBookingsApplicationService bookings) {
    this.bookings = bookings;
  }

  @PostMapping("/bookings")
  @ResponseStatus(HttpStatus.CREATED)
  RestTableBooking book(@RequestBody RestBookingToCreate booking) {
    return RestTableBooking.from(bookings.book(booking.toDomain()));
  }

  @GetMapping("/bookings/{bookingId}")
  RestTableBooking booking(@PathVariable UUID bookingId) {
    return RestTableBooking.from(bookings.booking(new TableBookingId(bookingId)));
  }

  @GetMapping("/bookings")
  List<RestTableBooking> dayBookings(@RequestParam LocalDate date, @RequestParam MealService service) {
    return bookings.dayBookings(date, service).stream().map(RestTableBooking::from).toList();
  }

  @PostMapping("/bookings/{bookingId}/deposit-payment")
  RestTableBooking payDeposit(@PathVariable UUID bookingId, @RequestBody RestDepositPayment payment) {
    return RestTableBooking.from(bookings.payDeposit(new TableBookingId(bookingId), payment.amount()));
  }

  @PostMapping("/bookings/{bookingId}/cancellation")
  RestTableBooking cancel(@PathVariable UUID bookingId) {
    return RestTableBooking.from(bookings.cancel(new TableBookingId(bookingId)));
  }

  @PostMapping("/bookings/{bookingId}/check-in")
  RestTableBooking checkIn(@PathVariable UUID bookingId) {
    return RestTableBooking.from(bookings.checkIn(new TableBookingId(bookingId)));
  }

  @PostMapping("/bookings/{bookingId}/no-show")
  RestTableBooking noShow(@PathVariable UUID bookingId) {
    return RestTableBooking.from(bookings.noShow(new TableBookingId(bookingId)));
  }

  @GetMapping("/availability")
  List<RestAvailableSlot> availability(@RequestParam LocalDate date, @RequestParam MealService service, @RequestParam int people) {
    return bookings.availability(date, service, new PartySize(people)).stream().map(RestAvailableSlot::from).toList();
  }
}
