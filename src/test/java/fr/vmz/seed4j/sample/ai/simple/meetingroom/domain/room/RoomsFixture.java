package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

public final class RoomsFixture {

  private RoomsFixture() {}

  public static Room everest() {
    return room("Everest");
  }

  public static Room room(String name) {
    return new RoomToCreate(new RoomName(name)).create();
  }
}
