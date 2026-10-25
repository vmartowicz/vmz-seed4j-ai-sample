package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ScheduleFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import org.junit.jupiter.api.Test;

@UnitTest
class TeachingSlotTest {

  private final TeachingSlot nineToTen = slot(Gym.ECULLY, "2026-03-09T09:00", 60);

  @Test
  void shouldMeasureMinutes() {
    assertThat(slot(Gym.ECULLY, "2026-03-09T09:00", 45).minutes()).isEqualTo(45);
  }

  @Test
  void shouldNeed15MinutesBetweenClassesInTheSameGym() {
    assertThat(nineToTen.isTooCloseTo(slot(Gym.ECULLY, "2026-03-09T10:14", 60))).isTrue();
    assertThat(nineToTen.isTooCloseTo(slot(Gym.ECULLY, "2026-03-09T10:15", 60))).isFalse();
    assertThat(nineToTen.isTooCloseTo(slot(Gym.ECULLY, "2026-03-09T07:45", 60))).isFalse();
    assertThat(nineToTen.isTooCloseTo(slot(Gym.ECULLY, "2026-03-09T07:46", 60))).isTrue();
  }

  @Test
  void shouldNeed30MinutesWhenChangingGym() {
    assertThat(nineToTen.isTooCloseTo(slot(Gym.PART_DIEU, "2026-03-09T10:29", 60))).isTrue();
    assertThat(nineToTen.isTooCloseTo(slot(Gym.PART_DIEU, "2026-03-09T10:30", 60))).isFalse();
  }

  @Test
  void shouldOverlapOnlyDuringClass() {
    assertThat(nineToTen.overlaps(slot(Gym.ECULLY, "2026-03-09T09:59", 60))).isTrue();
    assertThat(nineToTen.overlaps(slot(Gym.ECULLY, "2026-03-09T10:00", 60))).isFalse();
    assertThat(nineToTen.overlaps(slot(Gym.ECULLY, "2026-03-09T08:00", 60))).isFalse();
  }
}
