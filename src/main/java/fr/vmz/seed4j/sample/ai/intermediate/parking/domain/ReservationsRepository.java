package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationsRepository {
  void save(Reservation reservation);

  Optional<Reservation> get(ReservationId reservation);

  List<Reservation> activeOn(LocalDate date);

  List<Reservation> activeOf(ResidentName resident);

  default Reservation reservation(ReservationId reservation) {
    return get(reservation).orElseThrow(() -> new UnknownReservationException(reservation));
  }
}
