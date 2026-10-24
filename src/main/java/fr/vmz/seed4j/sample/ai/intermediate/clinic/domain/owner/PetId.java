package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record PetId(UUID id) {
  public PetId {
    Assert.notNull("id", id);
  }

  public static PetId newId() {
    return new PetId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
