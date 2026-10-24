package fr.vmz.seed4j.sample.ai.clinic.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record FirstName(String firstName) {
  public FirstName {
    Assert.field("firstName", firstName).notBlank().maxLength(30);
  }

  public String get() {
    return firstName();
  }
}
