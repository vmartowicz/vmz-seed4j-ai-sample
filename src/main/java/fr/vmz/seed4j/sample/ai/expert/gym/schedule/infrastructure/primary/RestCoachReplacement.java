package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.CoachId;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestCoachReplacement(@Nullable UUID coachId) {
  CoachId coach() {
    return new CoachId(coachId);
  }
}
