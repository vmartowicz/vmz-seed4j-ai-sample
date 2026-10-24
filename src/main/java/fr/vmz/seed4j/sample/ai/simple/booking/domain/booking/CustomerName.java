package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record CustomerName(String name) {
  public CustomerName {
    Assert.field("name", name).notBlank().maxLength(100);
  }

  public String get() {
    return name();
  }
}
