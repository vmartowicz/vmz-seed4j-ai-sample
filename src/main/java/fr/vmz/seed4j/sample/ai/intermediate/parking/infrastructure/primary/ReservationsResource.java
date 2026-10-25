package fr.vmz.seed4j.sample.ai.intermediate.parking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.parking.application.ReservationsApplicationService;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationId;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ResidentName;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.SpaceNumber;
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
@RequestMapping("/api/parking")
class ReservationsResource {

  private final ReservationsApplicationService reservations;

  ReservationsResource(ReservationsApplicationService reservations) {
    this.reservations = reservations;
  }

  @PostMapping("/reservations")
  @ResponseStatus(HttpStatus.CREATED)
  RestReservation reserve(@RequestBody RestReservationToCreate reservation) {
    return RestReservation.from(reservations.reserve(reservation.toDomain()));
  }

  @PostMapping("/reservations/{reservationId}/cancellation")
  RestReservation cancel(@PathVariable UUID reservationId) {
    return RestReservation.from(reservations.cancel(new ReservationId(reservationId)));
  }

  @GetMapping("/reservations")
  List<RestReservation> currentReservations(@RequestParam String resident) {
    return reservations.currentReservationsOf(new ResidentName(resident)).stream().map(RestReservation::from).toList();
  }

  @GetMapping("/spaces/available")
  List<Integer> availableSpaces(@RequestParam LocalDate date) {
    return reservations.availableSpaces(date).stream().map(SpaceNumber::get).toList();
  }
}
