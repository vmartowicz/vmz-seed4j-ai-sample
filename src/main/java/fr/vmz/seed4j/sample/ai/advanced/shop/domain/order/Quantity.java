package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Quantity(int quantity) {
  public Quantity {
    Assert.field("quantity", quantity).min(1);
  }

  public int get() {
    return quantity();
  }
}
