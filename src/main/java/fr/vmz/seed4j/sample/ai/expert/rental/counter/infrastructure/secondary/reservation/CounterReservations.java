package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.secondary.reservation;

import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.BookedRental;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.Reservations;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.primary.JavaReservations;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class CounterReservations implements Reservations {

  private final JavaReservations reservations;

  CounterReservations(JavaReservations reservations) {
    this.reservations = reservations;
  }

  @Override
  public Optional<BookedRental> reservation(UUID reservation) {
    return reservations
      .reservation(reservation)
      .map(booked ->
        new BookedRental(
          booked.id(),
          new Email(booked.driverEmail()),
          booked.youngDriver(),
          booked.category(),
          booked.pickUpPoint(),
          booked.pickUpAt(),
          booked.returnAt(),
          booked.days(),
          booked.dailyRate(),
          booked.paid(),
          booked.confirmed()
        )
      );
  }

  @Override
  public void markPickedUp(UUID reservation) {
    reservations.markPickedUp(reservation);
  }
}
