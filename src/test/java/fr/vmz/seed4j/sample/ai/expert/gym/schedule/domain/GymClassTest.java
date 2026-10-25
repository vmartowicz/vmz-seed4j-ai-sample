package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ScheduleFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class GymClassTest {

  private final Coach coach = yogaCoach();

  @Test
  void shouldScheduleWeeklyClass() {
    var gymClass = yogaOn(coach, DayOfWeek.WEDNESDAY, "18:30").schedule();

    assertThat(gymClass.id()).isNotNull();
    assertThat(gymClass.discipline()).isEqualTo(Discipline.YOGA);
    assertThat(gymClass.coach()).isEqualTo(coach.id());
    assertThat(gymClass.studio()).isEqualTo(studioA());
    assertThat(gymClass.day()).isEqualTo(DayOfWeek.WEDNESDAY);
    assertThat(gymClass.start()).isEqualTo(LocalTime.of(18, 30));
    assertThat(gymClass.minutes()).isEqualTo(60);
    assertThat(gymClass.capacity()).isEqualTo(20);
  }

  @Test
  void shouldPlaceClassInWeek() {
    var slot = yogaOn(coach, DayOfWeek.WEDNESDAY, "18:30").schedule().slotIn(MONDAY);

    assertThat(slot).isEqualTo(new TeachingSlot(Gym.PART_DIEU, LocalDateTime.of(2026, 3, 11, 18, 30), LocalDateTime.of(2026, 3, 11, 19, 30)));
  }

  @ParameterizedTest
  @ValueSource(ints = { 45, 60 })
  void shouldLast45Or60Minutes(int minutes) {
    var gymClass = new ClassToSchedule(Discipline.YOGA, coach.id(), studioA(), DayOfWeek.MONDAY, LocalTime.NOON, minutes, 20);

    assertThat(gymClass.schedule().minutes()).isEqualTo(minutes);
  }

  @Test
  void shouldNotLastOtherDurations() {
    assertThatThrownBy(() -> new ClassToSchedule(Discipline.YOGA, coach.id(), studioA(), DayOfWeek.MONDAY, LocalTime.NOON, 30, 20))
      .isExactlyInstanceOf(InvalidClassDurationException.class)
      .hasMessageContaining("30");
  }

  @Test
  void shouldNotExceedStudioCapacity() {
    assertThatThrownBy(() -> new ClassToSchedule(Discipline.YOGA, coach.id(), studioA(), DayOfWeek.MONDAY, LocalTime.NOON, 60, 31))
      .isExactlyInstanceOf(CapacityExceedsStudioException.class)
      .hasMessageContaining("30");
  }

  @Test
  void shouldHaveAtLeastOnePlace() {
    assertThatThrownBy(() -> new ClassToSchedule(Discipline.YOGA, coach.id(), studioA(), DayOfWeek.MONDAY, LocalTime.NOON, 60, 0)).isInstanceOf(
      AssertionException.class
    );
  }
}
