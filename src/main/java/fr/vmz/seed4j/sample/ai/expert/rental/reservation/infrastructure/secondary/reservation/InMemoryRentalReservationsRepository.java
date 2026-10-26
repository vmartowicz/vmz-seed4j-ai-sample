package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.secondary.reservation;

import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.Reservation;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationId;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationsRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryRentalReservationsRepository implements ReservationsRepository {

  private final Map<ReservationId, Reservation> reservations = new ConcurrentHashMap<>();

  @Override
  public void save(Reservation reservation) {
    reservations.put(reservation.id(), reservation);
  }

  @Override
  public Optional<Reservation> get(ReservationId reservation) {
    return Optional.ofNullable(reservations.get(reservation));
  }
}
