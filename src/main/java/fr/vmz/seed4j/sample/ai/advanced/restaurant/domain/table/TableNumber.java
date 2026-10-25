package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record TableNumber(int number) {
  public TableNumber {
    Assert.field("number", number).min(1).max(18);
  }

  public int get() {
    return number();
  }
}
