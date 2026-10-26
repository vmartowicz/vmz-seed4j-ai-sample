package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.reservation.application.RentalReservationsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationId;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rental/reservations")
class RentalReservationsResource {

  private final RentalReservationsApplicationService reservations;

  RentalReservationsResource(RentalReservationsApplicationService reservations) {
    this.reservations = reservations;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestReservation book(@RequestBody RestReservationToBook booking) {
    return RestReservation.from(reservations.book(booking.toDomain()));
  }

  @GetMapping("/{reservationId}")
  RestReservation reservation(@PathVariable UUID reservationId) {
    return RestReservation.from(reservations.reservation(new ReservationId(reservationId)));
  }

  @PostMapping("/{reservationId}/cancellation")
  RestReservation cancel(@PathVariable UUID reservationId) {
    return RestReservation.from(reservations.cancel(new ReservationId(reservationId)));
  }
}
