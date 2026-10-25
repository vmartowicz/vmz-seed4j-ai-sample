package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

@UnitTest
class HourSlotTest {

  // 2026-03-10 is a Tuesday
  private static final LocalDateTime TUESDAY_10_AM = LocalDateTime.of(2026, 3, 10, 10, 0);

  @Test
  void shouldLastOneHour() {
    var slot = new HourSlot(TUESDAY_10_AM);

    assertThat(slot.start()).isEqualTo(TUESDAY_10_AM);
    assertThat(slot.end()).isEqualTo(TUESDAY_10_AM.plusHours(1));
  }

  @Test
  void shouldStartAt8AtTheEarliest() {
    assertThat(new HourSlot(TUESDAY_10_AM.withHour(8)).start().getHour()).isEqualTo(8);
  }

  @Test
  void shouldNotStartBefore8() {
    assertThatThrownBy(() -> new HourSlot(TUESDAY_10_AM.withHour(7)))
      .isExactlyInstanceOf(InvalidHourSlotException.class)
      .hasMessageContaining("2026-03-10T07:00");
  }

  @Test
  void shouldStartAt18AtTheLatest() {
    assertThat(new HourSlot(TUESDAY_10_AM.withHour(18)).end().getHour()).isEqualTo(19);
  }

  @Test
  void shouldNotStartAfter18() {
    assertThatThrownBy(() -> new HourSlot(TUESDAY_10_AM.withHour(19))).isExactlyInstanceOf(InvalidHourSlotException.class);
  }

  @Test
  void shouldStartOnTheHour() {
    assertThatThrownBy(() -> new HourSlot(TUESDAY_10_AM.withMinute(30))).isExactlyInstanceOf(InvalidHourSlotException.class);
  }

  @Test
  void shouldBeAvailableOnFriday() {
    assertThat(new HourSlot(TUESDAY_10_AM.plusDays(3)).start().getDayOfWeek()).hasToString("FRIDAY");
  }

  @Test
  void shouldNotBeAvailableOnSaturday() {
    assertThatThrownBy(() -> new HourSlot(TUESDAY_10_AM.plusDays(4))).isExactlyInstanceOf(InvalidHourSlotException.class);
  }
}
