package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table;

import static fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.TablePosition.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class DiningRoom {

  private static final Set<Month> SUMMER = Set.of(Month.JUNE, Month.JULY, Month.AUGUST);

  private static final List<DiningTable> TABLES = Stream.of(
    range(1, 4, 2, WINDOW),
    range(5, 7, 2, TERRACE),
    range(8, 10, 2, INDOOR),
    range(11, 12, 4, WINDOW),
    range(13, 14, 4, TERRACE),
    range(15, 16, 4, INDOOR),
    range(17, 17, 6, TERRACE),
    range(18, 18, 6, INDOOR)
  )
    .flatMap(List::stream)
    .toList();

  private DiningRoom() {}

  public static List<DiningTable> tables() {
    return TABLES;
  }

  public static TablePosition preferredPosition(LocalDate date, PartySize party, boolean loyal) {
    if (SUMMER.contains(date.getMonth())) {
      return TERRACE;
    }

    if (party.isCouple() || loyal) {
      return WINDOW;
    }

    return INDOOR;
  }

  public static List<DiningTable> seat(PartySize party, Collection<DiningTable> occupied, TablePosition preferred) {
    var seats = seatsPerTable(party);
    var count = party.get() > 6 ? 2 : 1;

    var candidates = TABLES.stream()
      .filter(table -> table.seats() == seats)
      .filter(table -> !occupied.contains(table))
      .sorted(Comparator.comparing((DiningTable table) -> table.position() != preferred).thenComparing(table -> table.number().get()))
      .limit(count)
      .toList();

    if (candidates.size() < count) {
      return List.of();
    }

    return candidates;
  }

  private static int seatsPerTable(PartySize party) {
    if (party.isCouple()) {
      return 2;
    }

    if (party.get() <= 4 || party.get() > 6) {
      return 4;
    }

    return 6;
  }

  private static List<DiningTable> range(int first, int last, int seats, TablePosition position) {
    return IntStream.rangeClosed(first, last)
      .mapToObj(number -> new DiningTable(new TableNumber(number), seats, position))
      .toList();
  }
}
