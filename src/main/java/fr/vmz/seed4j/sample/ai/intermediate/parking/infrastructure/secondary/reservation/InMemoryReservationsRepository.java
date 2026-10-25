package fr.vmz.seed4j.sample.ai.intermediate.parking.infrastructure.secondary.reservation;

import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.Reservation;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationId;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationStatus;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationsRepository;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ResidentName;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryReservationsRepository implements ReservationsRepository {

  private final Map<ReservationId, Reservation> reservations = new ConcurrentHashMap<>();

  @Override
  public void save(Reservation reservation) {
    reservations.put(reservation.id(), reservation);
  }

  @Override
  public Optional<Reservation> get(ReservationId reservation) {
    return Optional.ofNullable(reservations.get(reservation));
  }

  @Override
  public List<Reservation> activeOn(LocalDate date) {
    return active(reservation -> reservation.date().equals(date));
  }

  @Override
  public List<Reservation> activeOf(ResidentName resident) {
    return active(reservation -> reservation.resident().sameAs(resident));
  }

  private List<Reservation> active(Predicate<Reservation> filter) {
    return reservations
      .values()
      .stream()
      .filter(reservation -> reservation.status() == ReservationStatus.ACTIVE)
      .filter(filter)
      .toList();
  }
}
