package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record ClassToSchedule(
  Discipline discipline,
  CoachId coach,
  Studio studio,
  DayOfWeek day,
  LocalTime start,
  int minutes,
  int capacity
) {
  private static final Set<Integer> DURATIONS = Set.of(45, 60);

  public ClassToSchedule {
    Assert.notNull("discipline", discipline);
    Assert.notNull("coach", coach);
    Assert.notNull("studio", studio);
    Assert.notNull("day", day);
    Assert.notNull("start", start);
    Assert.field("capacity", capacity).min(1);

    if (!DURATIONS.contains(minutes)) {
      throw new InvalidClassDurationException(minutes);
    }

    if (capacity > studio.capacity()) {
      throw new CapacityExceedsStudioException(studio, capacity);
    }
  }

  public GymClass schedule() {
    return new GymClass(GymClassId.newId(), this);
  }
}
