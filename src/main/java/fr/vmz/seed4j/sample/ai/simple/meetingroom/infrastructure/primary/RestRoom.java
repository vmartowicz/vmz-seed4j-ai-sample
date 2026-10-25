package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.Room;
import java.util.UUID;

record RestRoom(UUID id, String name) {
  static RestRoom from(Room room) {
    return new RestRoom(room.id().get(), room.name().get());
  }
}
