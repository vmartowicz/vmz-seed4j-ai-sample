package fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog;

import static fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.CatalogFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

@UnitTest
class BookableServiceTest {

  // 2026-03-10 is a Tuesday
  private static final LocalDateTime TUESDAY_10_AM = LocalDateTime.of(2026, 3, 10, 10, 0);

  @Test
  void shouldBuildSlotOfServiceDuration() {
    var slot = HAIRCUT.slotStartingAt(TUESDAY_10_AM);

    assertThat(slot.start()).isEqualTo(TUESDAY_10_AM);
    assertThat(slot.end()).isEqualTo(TUESDAY_10_AM.plusMinutes(30));
  }

  @Test
  void shouldAcceptFirstSlotAtOpening() {
    assertThat(MASSAGE.slotStartingAt(TUESDAY_10_AM.withHour(9)).start()).isEqualTo(TUESDAY_10_AM.withHour(9));
  }

  @Test
  void shouldAcceptLastSlotEndingAtClosing() {
    assertThat(HAIRCUT.slotStartingAt(TUESDAY_10_AM.withHour(17).withMinute(30)).end()).isEqualTo(TUESDAY_10_AM.withHour(18));
  }

  @Test
  void shouldAcceptSaturday() {
    assertThat(HAIRCUT.slotStartingAt(TUESDAY_10_AM.plusDays(4)).start().getDayOfWeek()).hasToString("SATURDAY");
  }

  @Test
  void shouldNotBookOnSunday() {
    assertThatThrownBy(() -> HAIRCUT.slotStartingAt(TUESDAY_10_AM.plusDays(5)))
      .isExactlyInstanceOf(InvalidSlotException.class)
      .hasMessageContaining("2026-03-15T10:00");
  }

  @Test
  void shouldNotBookBeforeOpening() {
    assertThatThrownBy(() -> HAIRCUT.slotStartingAt(TUESDAY_10_AM.withHour(8).withMinute(30))).isExactlyInstanceOf(
      InvalidSlotException.class
    );
  }

  @Test
  void shouldNotBookSlotEndingAfterClosing() {
    assertThatThrownBy(() -> HAIRCUT.slotStartingAt(TUESDAY_10_AM.withHour(18))).isExactlyInstanceOf(InvalidSlotException.class);
  }

  @Test
  void shouldNotBookSlotEndingAfterMidnight() {
    assertThatThrownBy(() -> HAIRCUT.slotStartingAt(TUESDAY_10_AM.withHour(23).withMinute(30))).isExactlyInstanceOf(
      InvalidSlotException.class
    );
  }

  @Test
  void shouldNotBookSlotNotAlignedOnServiceDuration() {
    assertThatThrownBy(() -> MASSAGE.slotStartingAt(TUESDAY_10_AM.withMinute(30))).isExactlyInstanceOf(InvalidSlotException.class);
  }
}
