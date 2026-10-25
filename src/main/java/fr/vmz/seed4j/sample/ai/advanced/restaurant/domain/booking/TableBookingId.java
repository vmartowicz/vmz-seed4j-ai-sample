package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record TableBookingId(UUID id) {
  public TableBookingId {
    Assert.notNull("id", id);
  }

  public static TableBookingId newId() {
    return new TableBookingId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
