package fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.program.application.ProgramApplicationService;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaProgram {

  private final ProgramApplicationService program;

  public JavaProgram(ProgramApplicationService program) {
    this.program = program;
  }

  public void talkWithdrawn(UUID talk) {
    program.withdraw(talk);
  }

  public Optional<JavaSession> sessionAt(Room room, LocalDateTime at) {
    return program.sessionAt(room, at).map(JavaSession::from);
  }
}
