package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record ProductId(UUID id) {
  public ProductId {
    Assert.notNull("id", id);
  }

  public static ProductId newId() {
    return new ProductId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
