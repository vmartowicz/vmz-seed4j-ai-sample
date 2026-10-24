package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record VisitId(UUID id) {
  public VisitId {
    Assert.notNull("id", id);
  }

  public static VisitId newId() {
    return new VisitId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
