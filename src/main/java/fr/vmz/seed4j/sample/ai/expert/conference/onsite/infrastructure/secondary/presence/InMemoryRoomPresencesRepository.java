package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.secondary.presence;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.BadgeId;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomPresence;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomPresencesRepository;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryRoomPresencesRepository implements RoomPresencesRepository {

  private final Map<UUID, RoomPresence> presences = new ConcurrentHashMap<>();

  @Override
  public void save(RoomPresence presence) {
    presences.put(presence.id(), presence);
  }

  @Override
  public Optional<RoomPresence> openOf(BadgeId badge) {
    return presences
      .values()
      .stream()
      .filter(RoomPresence::isOpen)
      .filter(presence -> presence.badge().equals(badge))
      .findFirst();
  }

  @Override
  public List<RoomPresence> openIn(Room room) {
    return presences
      .values()
      .stream()
      .filter(RoomPresence::isOpen)
      .filter(presence -> presence.room() == room)
      .toList();
  }

  @Override
  public List<RoomPresence> all() {
    return List.copyOf(presences.values());
  }
}
