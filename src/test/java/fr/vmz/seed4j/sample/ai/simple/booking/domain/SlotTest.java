package fr.vmz.seed4j.sample.ai.simple.booking.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

@UnitTest
class SlotTest {

  private static final LocalDateTime TEN = LocalDateTime.of(2026, 3, 12, 10, 0);
  private static final Slot TEN_TO_ELEVEN = new Slot(TEN, TEN.plusHours(1));

  @Test
  void shouldOverlapSlotStartingDuringIt() {
    assertThat(TEN_TO_ELEVEN.overlaps(new Slot(TEN.plusMinutes(30), TEN.plusMinutes(60)))).isTrue();
  }

  @Test
  void shouldNotOverlapFollowingSlot() {
    assertThat(TEN_TO_ELEVEN.overlaps(new Slot(TEN.plusHours(1), TEN.plusHours(2)))).isFalse();
  }

  @Test
  void shouldNotOverlapPreviousSlot() {
    assertThat(TEN_TO_ELEVEN.overlaps(new Slot(TEN.minusMinutes(30), TEN))).isFalse();
  }
}
