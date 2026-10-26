package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record ConsultationId(UUID id) {
  public ConsultationId {
    Assert.notNull("consultationId", id);
  }

  public static ConsultationId newId() {
    return new ConsultationId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
