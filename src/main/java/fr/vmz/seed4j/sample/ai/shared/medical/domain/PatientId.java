package fr.vmz.seed4j.sample.ai.shared.medical.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record PatientId(UUID id) {
  public PatientId {
    Assert.notNull("patientId", id);
  }

  public static PatientId newId() {
    return new PatientId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
