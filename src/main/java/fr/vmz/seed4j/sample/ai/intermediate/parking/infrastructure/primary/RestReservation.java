package fr.vmz.seed4j.sample.ai.intermediate.parking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.Reservation;
import java.time.LocalDate;
import java.util.UUID;

record RestReservation(UUID id, String residentName, int spaceNumber, LocalDate date, String status) {
  static RestReservation from(Reservation reservation) {
    return new RestReservation(
      reservation.id().get(),
      reservation.resident().get(),
      reservation.space().get(),
      reservation.date(),
      reservation.status().name()
    );
  }
}
