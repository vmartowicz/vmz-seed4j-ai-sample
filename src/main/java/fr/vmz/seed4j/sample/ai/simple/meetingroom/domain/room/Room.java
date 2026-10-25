package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

public final class Room {

  private final RoomId id;
  private final RoomName name;

  Room(RoomId id, RoomName name) {
    this.id = id;
    this.name = name;
  }

  public RoomId id() {
    return id;
  }

  public RoomName name() {
    return name;
  }
}
