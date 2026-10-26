package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.time.LocalDateTime;

public record Addendum(DoctorId author, String text, LocalDateTime writtenAt) {
  public Addendum {
    Assert.notNull("author", author);
    Assert.notBlank("text", text);
    Assert.notNull("writtenAt", writtenAt);
  }
}
