package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record RoomId(UUID id) {
  public RoomId {
    Assert.notNull("id", id);
  }

  public static RoomId newId() {
    return new RoomId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
