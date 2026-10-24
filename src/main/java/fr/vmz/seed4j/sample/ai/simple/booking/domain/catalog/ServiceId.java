package fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record ServiceId(UUID id) {
  public ServiceId {
    Assert.notNull("id", id);
  }

  public UUID get() {
    return id();
  }
}
