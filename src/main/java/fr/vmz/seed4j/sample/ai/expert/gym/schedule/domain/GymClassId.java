package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record GymClassId(UUID id) {
  public GymClassId {
    Assert.notNull("classId", id);
  }

  public static GymClassId newId() {
    return new GymClassId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
