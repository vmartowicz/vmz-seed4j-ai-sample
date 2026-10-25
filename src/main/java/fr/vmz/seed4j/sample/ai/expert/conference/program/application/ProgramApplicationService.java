package fr.vmz.seed4j.sample.ai.expert.conference.program.application;

import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ConfirmedTalks;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramBoard;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramSession;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramSessionsRepository;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.Slot;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ProgramApplicationService {

  private final ProgramBoard board;

  public ProgramApplicationService(ProgramSessionsRepository sessions, ConfirmedTalks talks) {
    board = new ProgramBoard(sessions, talks);
  }

  public ProgramSession place(UUID talk, Room room, Slot slot) {
    return board.place(talk, room, slot);
  }

  public void withdraw(UUID talk) {
    board.withdraw(talk);
  }

  public Optional<ProgramSession> sessionAt(Room room, LocalDateTime at) {
    return board.sessionAt(room, at);
  }

  public List<ProgramSession> day(LocalDate day) {
    return board.day(day);
  }
}
