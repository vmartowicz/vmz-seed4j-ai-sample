package fr.vmz.seed4j.sample.ai.shared.bank.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record CustomerId(UUID id) {
  public CustomerId {
    Assert.notNull("customerId", id);
  }

  public static CustomerId newId() {
    return new CustomerId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
