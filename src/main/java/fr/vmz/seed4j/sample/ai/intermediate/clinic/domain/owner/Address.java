package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Address(String address) {
  public Address {
    Assert.field("address", address).notBlank().maxLength(255);
  }

  public String get() {
    return address();
  }
}
