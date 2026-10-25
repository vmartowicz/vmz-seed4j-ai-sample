package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ClassSession;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.Discipline;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.LocalDateTime;
import java.util.UUID;

record RestClassSession(
  UUID id,
  UUID classId,
  Discipline discipline,
  UUID coachId,
  Gym gym,
  String studio,
  LocalDateTime start,
  LocalDateTime end,
  int capacity,
  boolean cancelled
) {
  static RestClassSession from(ClassSession session) {
    return new RestClassSession(
      session.id().get(),
      session.gymClass().get(),
      session.discipline(),
      session.coach().get(),
      session.studio().gym(),
      session.studio().name(),
      session.start(),
      session.end(),
      session.capacity(),
      session.isCancelled()
    );
  }
}
