package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.util.List;
import java.util.Optional;

public interface RoomPresencesRepository {
  void save(RoomPresence presence);

  Optional<RoomPresence> openOf(BadgeId badge);

  List<RoomPresence> openIn(Room room);

  List<RoomPresence> all();
}
