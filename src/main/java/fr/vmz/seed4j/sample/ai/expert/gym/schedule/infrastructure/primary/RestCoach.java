package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.Coach;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.Discipline;
import java.util.Set;
import java.util.UUID;

record RestCoach(UUID id, String name, Set<Discipline> certifications) {
  static RestCoach from(Coach coach) {
    return new RestCoach(coach.id().get(), coach.name(), coach.certifications());
  }
}
