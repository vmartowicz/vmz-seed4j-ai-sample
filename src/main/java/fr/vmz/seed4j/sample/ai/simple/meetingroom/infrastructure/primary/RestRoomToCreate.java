package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomName;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomToCreate;
import org.jspecify.annotations.Nullable;

record RestRoomToCreate(@Nullable String name) {
  RoomToCreate toDomain() {
    return new RoomToCreate(new RoomName(name));
  }
}
