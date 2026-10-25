package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public class RoomsCreator {

  private final RoomsRepository rooms;

  public RoomsCreator(RoomsRepository rooms) {
    this.rooms = rooms;
  }

  public Room create(RoomToCreate toCreate) {
    Assert.notNull("toCreate", toCreate);

    if (rooms.all().stream().anyMatch(room -> room.name().sameAs(toCreate.name()))) {
      throw new DuplicatedRoomNameException(toCreate.name());
    }

    var room = toCreate.create();
    rooms.save(room);

    return room;
  }
}
