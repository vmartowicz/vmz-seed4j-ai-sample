package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Suspension;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestSuspension(@Nullable LocalDate start, int months) {
  Suspension toDomain() {
    return new Suspension(start, months);
  }
}
