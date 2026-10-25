package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record RoomToCreate(RoomName name) {
  public RoomToCreate {
    Assert.notNull("name", name);
  }

  public Room create() {
    return new Room(RoomId.newId(), name);
  }
}
