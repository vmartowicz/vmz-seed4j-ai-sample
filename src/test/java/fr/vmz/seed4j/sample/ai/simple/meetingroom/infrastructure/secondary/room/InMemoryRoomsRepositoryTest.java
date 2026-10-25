package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.secondary.room;

import static fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryRoomsRepositoryTest {

  private final InMemoryRoomsRepository rooms = new InMemoryRoomsRepository();

  @Test
  void shouldGetSavedRoom() {
    var room = everest();
    rooms.save(room);

    assertThat(rooms.get(room.id())).contains(room);
    assertThat(rooms.get(RoomId.newId())).isEmpty();
  }

  @Test
  void shouldListRoomsByName() {
    var kilimanjaro = room("Kilimanjaro");
    var everest = everest();
    var annapurna = room("annapurna");
    rooms.save(kilimanjaro);
    rooms.save(everest);
    rooms.save(annapurna);

    assertThat(rooms.all()).containsExactly(annapurna, everest, kilimanjaro);
  }
}
