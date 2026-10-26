package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import java.util.Optional;

public interface ReservationsRepository {
  void save(Reservation reservation);

  Optional<Reservation> get(ReservationId reservation);

  default Reservation reservation(ReservationId reservation) {
    return get(reservation).orElseThrow(() -> new UnknownReservationException(reservation));
  }
}
