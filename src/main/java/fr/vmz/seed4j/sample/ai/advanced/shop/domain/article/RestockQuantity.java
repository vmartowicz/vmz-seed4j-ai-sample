package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record RestockQuantity(int units) {
  public RestockQuantity {
    Assert.field("units", units).min(1);
  }

  public int get() {
    return units();
  }
}
