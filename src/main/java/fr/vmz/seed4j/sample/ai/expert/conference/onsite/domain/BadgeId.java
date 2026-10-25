package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record BadgeId(UUID id) {
  public BadgeId {
    Assert.notNull("badgeId", id);
  }

  public static BadgeId newId() {
    return new BadgeId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
