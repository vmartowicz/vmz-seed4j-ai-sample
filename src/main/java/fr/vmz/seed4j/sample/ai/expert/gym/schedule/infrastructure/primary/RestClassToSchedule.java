package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ClassToSchedule;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.CoachId;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.Discipline;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.Studios;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestClassToSchedule(
  @Nullable Discipline discipline,
  @Nullable UUID coachId,
  @Nullable Gym gym,
  @Nullable String studio,
  @Nullable DayOfWeek day,
  @Nullable LocalTime start,
  int durationMinutes,
  int capacity
) {
  ClassToSchedule toDomain() {
    return new ClassToSchedule(discipline, new CoachId(coachId), Studios.studio(gym, studio), day, start, durationMinutes, capacity);
  }
}
