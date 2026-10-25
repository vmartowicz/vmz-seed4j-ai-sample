package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.BadgeType;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.ConferenceBadge;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestConferenceBadge(UUID id, String name, BadgeType type, @Nullable String ticket, boolean active) {
  static RestConferenceBadge from(ConferenceBadge badge) {
    return new RestConferenceBadge(badge.id().get(), badge.name(), badge.type(), badge.ticket().orElse(null), badge.isActive());
  }
}
