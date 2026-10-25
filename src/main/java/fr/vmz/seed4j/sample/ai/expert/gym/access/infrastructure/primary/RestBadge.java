package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.BadgeNumber;
import org.jspecify.annotations.Nullable;

record RestBadge(@Nullable String badge) {
  BadgeNumber toDomain() {
    return new BadgeNumber(badge);
  }
}
