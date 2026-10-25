package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record OrderId(UUID id) {
  public OrderId {
    Assert.notNull("id", id);
  }

  public static OrderId newId() {
    return new OrderId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
