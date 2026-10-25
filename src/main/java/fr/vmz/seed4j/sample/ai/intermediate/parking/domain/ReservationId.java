package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record ReservationId(UUID id) {
  public ReservationId {
    Assert.notNull("id", id);
  }

  public static ReservationId newId() {
    return new ReservationId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
