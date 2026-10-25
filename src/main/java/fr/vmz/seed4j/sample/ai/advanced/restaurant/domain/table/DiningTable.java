package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record DiningTable(TableNumber number, int seats, TablePosition position) {
  public DiningTable {
    Assert.notNull("number", number);
    Assert.notNull("position", position);
  }
}
