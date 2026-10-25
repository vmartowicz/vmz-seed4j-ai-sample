package fr.vmz.seed4j.sample.ai.simple.meetingroom.application;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.Room;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomToCreate;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomsCreator;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomsRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomsApplicationService {

  private final RoomsRepository rooms;
  private final RoomsCreator creator;

  public RoomsApplicationService(RoomsRepository rooms) {
    this.rooms = rooms;
    creator = new RoomsCreator(rooms);
  }

  public Room create(RoomToCreate room) {
    return creator.create(room);
  }

  public List<Room> all() {
    return rooms.all();
  }
}
