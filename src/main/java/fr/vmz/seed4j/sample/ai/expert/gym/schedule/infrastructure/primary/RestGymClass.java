package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.Discipline;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.GymClass;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

record RestGymClass(
  UUID id,
  Discipline discipline,
  UUID coachId,
  Gym gym,
  String studio,
  DayOfWeek day,
  LocalTime start,
  int durationMinutes,
  int capacity
) {
  static RestGymClass from(GymClass gymClass) {
    return new RestGymClass(
      gymClass.id().get(),
      gymClass.discipline(),
      gymClass.coach().get(),
      gymClass.studio().gym(),
      gymClass.studio().name(),
      gymClass.day(),
      gymClass.start(),
      gymClass.minutes(),
      gymClass.capacity()
    );
  }
}
