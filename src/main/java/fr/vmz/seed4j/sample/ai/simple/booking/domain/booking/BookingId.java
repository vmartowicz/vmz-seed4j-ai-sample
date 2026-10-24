package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record BookingId(UUID id) {
  public BookingId {
    Assert.notNull("id", id);
  }

  public static BookingId newId() {
    return new BookingId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
