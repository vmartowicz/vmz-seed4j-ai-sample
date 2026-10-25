package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record CoachId(UUID id) {
  public CoachId {
    Assert.notNull("coachId", id);
  }

  public static CoachId newId() {
    return new CoachId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
