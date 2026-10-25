package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record SessionId(UUID id) {
  public SessionId {
    Assert.notNull("sessionId", id);
  }

  public static SessionId newId() {
    return new SessionId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
