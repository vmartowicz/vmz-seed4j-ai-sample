package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.secondary.room;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.Room;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomsRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryRoomsRepository implements RoomsRepository {

  private static final Comparator<Room> BY_NAME = Comparator.comparing(room -> room.name().get(), String.CASE_INSENSITIVE_ORDER);

  private final Map<RoomId, Room> rooms = new ConcurrentHashMap<>();

  @Override
  public void save(Room room) {
    rooms.put(room.id(), room);
  }

  @Override
  public Optional<Room> get(RoomId room) {
    return Optional.ofNullable(rooms.get(room));
  }

  @Override
  public List<Room> all() {
    return rooms.values().stream().sorted(BY_NAME).toList();
  }
}
