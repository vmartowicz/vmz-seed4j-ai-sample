package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record HolderName(String name) {
  public HolderName {
    Assert.field("name", name).notBlank().maxLength(100);
  }

  public String get() {
    return name();
  }
}
