package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record FullName(String name) {
  public FullName {
    Assert.field("name", name).notBlank().maxLength(100);
  }

  public String get() {
    return name();
  }
}
