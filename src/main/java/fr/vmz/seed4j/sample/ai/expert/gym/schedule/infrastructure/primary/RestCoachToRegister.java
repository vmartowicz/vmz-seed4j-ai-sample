package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.CoachToRegister;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.Discipline;
import java.util.Set;
import org.jspecify.annotations.Nullable;

record RestCoachToRegister(@Nullable String name, @Nullable Set<Discipline> certifications) {
  CoachToRegister toDomain() {
    return new CoachToRegister(name, certifications);
  }
}
