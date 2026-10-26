package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.reservation.application.RentalReservationsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaReservations {

  private final RentalReservationsApplicationService reservations;

  public JavaReservations(RentalReservationsApplicationService reservations) {
    this.reservations = reservations;
  }

  public Optional<JavaReservation> reservation(UUID reservation) {
    return reservations.find(new ReservationId(reservation)).map(JavaReservation::from);
  }

  public void markPickedUp(UUID reservation) {
    reservations.markPickedUp(new ReservationId(reservation));
  }
}
