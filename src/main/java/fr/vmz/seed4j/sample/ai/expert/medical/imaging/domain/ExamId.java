package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record ExamId(UUID id) {
  public ExamId {
    Assert.notNull("examId", id);
  }

  public static ExamId newId() {
    return new ExamId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
