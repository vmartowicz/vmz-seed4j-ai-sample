package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record SampleId(UUID id) {
  public SampleId {
    Assert.notNull("sampleId", id);
  }

  public static SampleId newId() {
    return new SampleId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
