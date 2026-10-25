package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ScheduleFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

@UnitTest
class CoachAgendaTest {

  private final Coach coach = yogaCoach();

  @Test
  void shouldNotTeachUncertifiedDiscipline() {
    var candidate = slot(Gym.ECULLY, "2026-03-09T09:00", 60);

    assertThatThrownBy(() -> CoachAgenda.assertCanTeach(coach, Discipline.BOXING, candidate, List.of()))
      .isExactlyInstanceOf(CoachNotCertifiedException.class)
      .hasMessageContaining("BOXING");
  }

  @Test
  void shouldTeachUpTo25HoursAWeek() {
    var candidate = slot(Gym.ECULLY, "2026-03-15T20:00", 60);

    assertThatCode(() -> CoachAgenda.assertCanTeach(coach, Discipline.YOGA, candidate, hours(24))).doesNotThrowAnyException();
  }

  @Test
  void shouldNotTeachMoreThan25HoursAWeek() {
    var candidate = slot(Gym.ECULLY, "2026-03-15T20:00", 60);
    var existing = new ArrayList<>(hours(24));
    existing.add(slot(Gym.ECULLY, "2026-03-15T08:00", 45));

    assertThatThrownBy(() -> CoachAgenda.assertCanTeach(coach, Discipline.YOGA, candidate, existing)).isExactlyInstanceOf(
      CoachOverbookedException.class
    );
  }

  @Test
  void shouldNotTeachTooCloseToAnotherClass() {
    var candidate = slot(Gym.PART_DIEU, "2026-03-09T10:15", 60);

    assertThatThrownBy(() ->
      CoachAgenda.assertCanTeach(coach, Discipline.YOGA, candidate, List.of(slot(Gym.ECULLY, "2026-03-09T09:00", 60)))
    ).isExactlyInstanceOf(CoachNotAvailableException.class);
  }

  private static List<TeachingSlot> hours(int hours) {
    return IntStream.range(0, hours)
      .mapToObj(hour -> slot(Gym.ECULLY, MONDAY.plusDays(hour / 6).atTime(8 + 2 * (hour % 6), 0).toString(), 60))
      .toList();
  }
}
