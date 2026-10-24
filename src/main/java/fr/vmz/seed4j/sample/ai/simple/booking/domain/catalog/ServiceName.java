package fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record ServiceName(String name) {
  public ServiceName {
    Assert.field("name", name).notBlank().maxLength(50);
  }

  public String get() {
    return name();
  }
}
