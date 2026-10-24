package fr.vmz.seed4j.sample.ai.clinic.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record LastName(String lastName) {
  public LastName {
    Assert.field("lastName", lastName).notBlank().maxLength(30);
  }

  public String get() {
    return lastName();
  }
}
