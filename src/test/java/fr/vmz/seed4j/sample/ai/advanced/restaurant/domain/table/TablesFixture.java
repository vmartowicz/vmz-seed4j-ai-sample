package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table;

import java.util.List;
import java.util.stream.IntStream;

public final class TablesFixture {

  private TablesFixture() {}

  public static DiningTable table(int number) {
    return DiningRoom.tables()
      .stream()
      .filter(table -> table.number().equals(new TableNumber(number)))
      .findFirst()
      .orElseThrow();
  }

  public static List<DiningTable> tables(int... numbers) {
    return IntStream.of(numbers).mapToObj(TablesFixture::table).toList();
  }
}
