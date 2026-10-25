package fr.vmz.seed4j.sample.ai.intermediate.parking.application;

import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.Reservation;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationId;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationToCreate;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationsRepository;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ResidentName;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.SpaceNumber;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.SpacesReserver;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservationsApplicationService {

  private final SpacesReserver reserver;
  private final Clock clock;

  public ReservationsApplicationService(ReservationsRepository reservations, Clock clock) {
    reserver = new SpacesReserver(reservations);
    this.clock = clock;
  }

  public Reservation reserve(ReservationToCreate reservation) {
    return reserver.reserve(reservation, LocalDate.now(clock));
  }

  public Reservation cancel(ReservationId reservation) {
    return reserver.cancel(reservation, LocalDateTime.now(clock));
  }

  public List<SpaceNumber> availableSpaces(LocalDate date) {
    return reserver.availableSpaces(date);
  }

  public List<Reservation> currentReservationsOf(ResidentName resident) {
    return reserver.currentReservationsOf(resident, LocalDate.now(clock));
  }
}
