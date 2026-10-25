package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record ClassBookingId(UUID id) {
  public ClassBookingId {
    Assert.notNull("bookingId", id);
  }

  public static ClassBookingId newId() {
    return new ClassBookingId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
