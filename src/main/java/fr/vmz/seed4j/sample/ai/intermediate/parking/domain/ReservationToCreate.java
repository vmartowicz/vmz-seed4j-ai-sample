package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public record ReservationToCreate(ResidentName resident, SpaceNumber space, LocalDate date) {
  public ReservationToCreate {
    Assert.notNull("resident", resident);
    Assert.notNull("space", space);
    Assert.notNull("date", date);
  }

  public Reservation create() {
    return new Reservation(ReservationId.newId(), this);
  }
}
