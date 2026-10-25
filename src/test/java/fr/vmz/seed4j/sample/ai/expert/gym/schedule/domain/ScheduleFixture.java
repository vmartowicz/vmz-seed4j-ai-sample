package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public final class ScheduleFixture {

  public static final LocalDate MONDAY = LocalDate.of(2026, 3, 9);

  private ScheduleFixture() {}

  public static Coach yogaCoach() {
    return new CoachToRegister("Alice Martin", Set.of(Discipline.YOGA, Discipline.PILATES)).register();
  }

  public static Studio studioA() {
    return Studios.studio(Gym.PART_DIEU, "A");
  }

  public static ClassToSchedule yogaOn(Coach coach, DayOfWeek day, String start) {
    return new ClassToSchedule(Discipline.YOGA, coach.id(), studioA(), day, LocalTime.parse(start), 60, 20);
  }

  public static TeachingSlot slot(Gym gym, String start, int minutes) {
    var at = LocalDateTime.parse(start);

    return new TeachingSlot(gym, at, at.plusMinutes(minutes));
  }
}
