package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class SlotTest {

  @ParameterizedTest
  @CsvSource({ "1, 09:00", "2, 09:50", "3, 10:40", "4, 11:30", "5, 14:00", "6, 14:50", "7, 15:40", "8, 16:30", "9, 17:20" })
  void shouldHave9SlotsOf40Minutes(int number, String start) {
    var slot = dayTwo(number);

    assertThat(slot.start()).isEqualTo(LocalDateTime.of(2026, 10, 16, 0, 0).with(LocalTime.parse(start)));
    assertThat(slot.end()).isEqualTo(slot.start().plusMinutes(40));
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 10 })
  void shouldNotBuildSlotOutsideTheDay(int number) {
    assertThatThrownBy(() -> dayOne(number)).isExactlyInstanceOf(InvalidSlotException.class);
  }

  @Test
  void shouldNotBuildSlotOutsideConference() {
    assertThatThrownBy(() -> new Slot(LocalDate.of(2026, 10, 17), 1)).isExactlyInstanceOf(InvalidSlotException.class).hasMessageContaining(
      "2026-10-17"
    );
  }

  @Test
  void shouldFollowSlotsWithinHalfDay() {
    assertThat(dayOne(3).next()).contains(dayOne(4));
    assertThat(dayOne(4).next()).isEmpty();
    assertThat(dayOne(8).next()).contains(dayOne(9));
    assertThat(dayOne(9).next()).isEmpty();
  }

  @Test
  void shouldReserveFirstSlotOfFirstDayForKeynote() {
    assertThat(dayOne(1).isKeynote()).isTrue();
    assertThat(dayOne(2).isKeynote()).isFalse();
    assertThat(dayTwo(1).isKeynote()).isFalse();
  }
}
