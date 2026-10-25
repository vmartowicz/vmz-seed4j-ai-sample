package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record CustomerOrderId(UUID id) {
  public CustomerOrderId {
    Assert.notNull("id", id);
  }

  public static CustomerOrderId newId() {
    return new CustomerOrderId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
