package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

import java.util.List;
import java.util.Optional;

public interface RoomsRepository {
  void save(Room room);

  Optional<Room> get(RoomId room);

  List<Room> all();

  default Room room(RoomId room) {
    return get(room).orElseThrow(() -> new UnknownRoomException(room));
  }
}
