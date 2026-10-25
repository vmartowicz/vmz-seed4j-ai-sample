package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.DiningTable;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.TablePosition;
import java.util.List;

record RestTable(int number, int seats, TablePosition position) {
  static List<RestTable> from(List<DiningTable> tables) {
    return tables
      .stream()
      .map(table -> new RestTable(table.number().get(), table.seats(), table.position()))
      .toList();
  }
}
