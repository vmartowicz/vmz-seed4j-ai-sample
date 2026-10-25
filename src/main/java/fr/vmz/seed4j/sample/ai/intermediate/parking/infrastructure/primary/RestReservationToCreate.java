package fr.vmz.seed4j.sample.ai.intermediate.parking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationToCreate;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ResidentName;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.SpaceNumber;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestReservationToCreate(@Nullable String residentName, int spaceNumber, @Nullable LocalDate date) {
  ReservationToCreate toDomain() {
    return new ReservationToCreate(new ResidentName(residentName), new SpaceNumber(spaceNumber), date);
  }
}
