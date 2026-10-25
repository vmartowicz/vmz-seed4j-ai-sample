package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table;

import static fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.TablesFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class DiningRoomTest {

  @Test
  void shouldHave18TablesFor56Covers() {
    var tables = DiningRoom.tables();

    assertThat(tables).hasSize(18);
    assertThat(tables.stream().mapToInt(DiningTable::seats).sum()).isEqualTo(56);
    assertThat(tables.stream().filter(table -> table.seats() == 2)).hasSize(10);
    assertThat(tables.stream().filter(table -> table.seats() == 4)).hasSize(6);
    assertThat(tables.stream().filter(table -> table.seats() == 6)).hasSize(2);
  }

  @Test
  void shouldPlaceTables() {
    assertThat(table(1).position()).isEqualTo(TablePosition.WINDOW);
    assertThat(table(5).position()).isEqualTo(TablePosition.TERRACE);
    assertThat(table(10).position()).isEqualTo(TablePosition.INDOOR);
    assertThat(table(12).position()).isEqualTo(TablePosition.WINDOW);
    assertThat(table(13).position()).isEqualTo(TablePosition.TERRACE);
    assertThat(table(16).position()).isEqualTo(TablePosition.INDOOR);
    assertThat(table(17).position()).isEqualTo(TablePosition.TERRACE);
    assertThat(table(18).position()).isEqualTo(TablePosition.INDOOR);
  }

  @Nested
  class Seating {

    @Test
    void shouldSeatCoupleAtFirstPreferredTable() {
      assertThat(numbers(DiningRoom.seat(new PartySize(2), List.of(), TablePosition.WINDOW))).containsExactly(1);
      assertThat(numbers(DiningRoom.seat(new PartySize(2), List.of(), TablePosition.TERRACE))).containsExactly(5);
    }

    @Test
    void shouldSeatAtLowestFreeTableWhenPreferredOnesAreTaken() {
      assertThat(numbers(DiningRoom.seat(new PartySize(2), tables(1, 2, 3, 4), TablePosition.WINDOW))).containsExactly(5);
    }

    @Test
    void shouldSeat3Or4PeopleAtTableFor4() {
      assertThat(numbers(DiningRoom.seat(new PartySize(3), List.of(), TablePosition.INDOOR))).containsExactly(15);
      assertThat(numbers(DiningRoom.seat(new PartySize(4), List.of(), TablePosition.WINDOW))).containsExactly(11);
    }

    @Test
    void shouldSeat5Or6PeopleAtTableFor6() {
      assertThat(numbers(DiningRoom.seat(new PartySize(5), List.of(), TablePosition.INDOOR))).containsExactly(18);
      assertThat(numbers(DiningRoom.seat(new PartySize(6), List.of(), TablePosition.TERRACE))).containsExactly(17);
    }

    @ParameterizedTest
    @ValueSource(ints = { 7, 8 })
    void shouldCombineTwoTablesFor4For7Or8People(int people) {
      assertThat(numbers(DiningRoom.seat(new PartySize(people), List.of(), TablePosition.INDOOR))).containsExactly(15, 16);
    }

    @Test
    void shouldNotSeat8PeopleWithOnlyOneTableFor4Left() {
      assertThat(DiningRoom.seat(new PartySize(8), tables(11, 12, 13, 14, 15), TablePosition.INDOOR)).isEmpty();
    }

    @Test
    void shouldNotSeatCoupleAtBiggerTable() {
      assertThat(DiningRoom.seat(new PartySize(2), tables(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), TablePosition.WINDOW)).isEmpty();
    }
  }

  @Nested
  class Preference {

    @ParameterizedTest
    @ValueSource(strings = { "2026-06-01", "2026-07-15", "2026-08-31" })
    void shouldPreferTerraceInSummer(String date) {
      assertThat(DiningRoom.preferredPosition(LocalDate.parse(date), new PartySize(2), true)).isEqualTo(TablePosition.TERRACE);
    }

    @Test
    void shouldPreferWindowForCouplesOutOfSummer() {
      assertThat(DiningRoom.preferredPosition(LocalDate.of(2026, 5, 31), new PartySize(2), false)).isEqualTo(TablePosition.WINDOW);
    }

    @Test
    void shouldPreferWindowForLoyalDinersOutOfSummer() {
      assertThat(DiningRoom.preferredPosition(LocalDate.of(2026, 9, 1), new PartySize(6), true)).isEqualTo(TablePosition.WINDOW);
    }

    @Test
    void shouldPreferIndoorForOtherGroupsOutOfSummer() {
      assertThat(DiningRoom.preferredPosition(LocalDate.of(2026, 9, 1), new PartySize(4), false)).isEqualTo(TablePosition.INDOOR);
    }
  }

  private static List<Integer> numbers(Collection<DiningTable> tables) {
    return tables.stream().map(table -> table.number().get()).toList();
  }
}
