package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.secondary.program;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomSession;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomSessions;
import fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.primary.JavaProgram;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class OnsiteRoomSessions implements RoomSessions {

  private final JavaProgram program;

  OnsiteRoomSessions(@Lazy JavaProgram program) {
    this.program = program;
  }

  @Override
  public Optional<RoomSession> sessionAt(Room room, LocalDateTime at) {
    return program
      .sessionAt(room, at)
      .map(session -> new RoomSession(session.talk(), session.title(), session.workshop(), session.speakers().stream().map(Email::new).toList()));
  }
}
