package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDateTime;
import java.util.Optional;

public interface RoomSessions {
  Optional<RoomSession> sessionAt(Room room, LocalDateTime at);
}
