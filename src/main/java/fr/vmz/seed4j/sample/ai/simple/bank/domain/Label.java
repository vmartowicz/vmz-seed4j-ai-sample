package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Label(String label) {
  public Label {
    Assert.field("label", label).notBlank().maxLength(100);
  }

  public String get() {
    return label();
  }
}
