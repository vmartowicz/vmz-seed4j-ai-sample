package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

import static fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class RoomsCreatorTest {

  private final RoomsRepository rooms = mock(RoomsRepository.class);
  private final RoomsCreator creator = new RoomsCreator(rooms);

  @Test
  void shouldCreateRoom() {
    when(rooms.all()).thenReturn(List.of(room("Kilimanjaro")));

    var room = creator.create(new RoomToCreate(new RoomName("Everest")));

    assertThat(room.id()).isNotNull();
    assertThat(room.name()).isEqualTo(new RoomName("Everest"));
    verify(rooms).save(room);
  }

  @Test
  void shouldNotCreateTwoRoomsWithSameNameIgnoringCase() {
    when(rooms.all()).thenReturn(List.of(everest()));

    assertThatThrownBy(() -> creator.create(new RoomToCreate(new RoomName("everest"))))
      .isExactlyInstanceOf(DuplicatedRoomNameException.class)
      .hasMessageContaining("everest");
    verify(rooms, never()).save(any());
  }
}
