package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record VisitDescription(String description) {
  public VisitDescription {
    Assert.field("description", description).notBlank().maxLength(255);
  }

  public String get() {
    return description();
  }
}
