package fr.vmz.seed4j.sample.ai.clinic.domain.vet;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record VetId(UUID id) {
  public VetId {
    Assert.notNull("id", id);
  }

  public static VetId newId() {
    return new VetId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
