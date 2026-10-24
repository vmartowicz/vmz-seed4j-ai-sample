package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record City(String city) {
  public City {
    Assert.field("city", city).notBlank().maxLength(80);
  }

  public String get() {
    return city();
  }
}
