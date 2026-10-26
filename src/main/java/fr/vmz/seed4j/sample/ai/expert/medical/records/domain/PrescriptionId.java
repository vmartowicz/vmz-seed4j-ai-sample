package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record PrescriptionId(UUID id) {
  public PrescriptionId {
    Assert.notNull("prescriptionId", id);
  }

  public static PrescriptionId newId() {
    return new PrescriptionId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
