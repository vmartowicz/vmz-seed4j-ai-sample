package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomPresence;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomSession;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestRoomPresence(Room room, LocalDateTime enteredAt, @Nullable LocalDateTime exitedAt, @Nullable UUID talkId) {
  static RestRoomPresence from(RoomPresence presence) {
    return new RestRoomPresence(
      presence.room(),
      presence.enteredAt(),
      presence.exitedAt().orElse(null),
      presence.session().map(RoomSession::talk).orElse(null)
    );
  }
}
