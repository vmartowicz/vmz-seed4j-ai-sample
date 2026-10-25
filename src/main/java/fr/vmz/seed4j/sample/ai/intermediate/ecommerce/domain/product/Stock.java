package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Stock(int quantity) {
  public Stock {
    Assert.field("stock", quantity).min(0);
  }

  public boolean covers(int requested) {
    return quantity >= requested;
  }

  public Stock minus(int removed) {
    return new Stock(quantity - removed);
  }

  public Stock plus(int added) {
    return new Stock(quantity + added);
  }

  public int get() {
    return quantity();
  }
}
