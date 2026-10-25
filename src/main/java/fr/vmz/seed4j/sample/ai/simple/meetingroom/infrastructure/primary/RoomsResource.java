package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.application.RoomsApplicationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meeting-rooms/rooms")
class RoomsResource {

  private final RoomsApplicationService rooms;

  RoomsResource(RoomsApplicationService rooms) {
    this.rooms = rooms;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestRoom create(@RequestBody RestRoomToCreate room) {
    return RestRoom.from(rooms.create(room.toDomain()));
  }

  @GetMapping
  List<RestRoom> list() {
    return rooms.all().stream().map(RestRoom::from).toList();
  }
}
