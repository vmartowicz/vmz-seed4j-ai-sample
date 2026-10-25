package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.BadgeId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestBadgeRequest(@Nullable String ticket, @Nullable String name, @Nullable UUID badgeId) {
  String ticketNumber() {
    Assert.notBlank("ticket", ticket);

    return ticket;
  }

  String volunteerName() {
    Assert.notBlank("name", name);

    return name;
  }

  BadgeId badge() {
    return new BadgeId(badgeId);
  }
}
