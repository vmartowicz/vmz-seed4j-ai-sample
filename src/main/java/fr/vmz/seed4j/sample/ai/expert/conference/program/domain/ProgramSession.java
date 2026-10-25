package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDateTime;
import java.util.List;

public record ProgramSession(TalkToSchedule talk, Room room, List<Slot> slots) {
  public ProgramSession {
    slots = List.copyOf(slots);
  }

  public LocalDateTime start() {
    return slots.getFirst().start();
  }

  public LocalDateTime end() {
    return slots.getLast().end();
  }

  public boolean covers(LocalDateTime at) {
    return !at.isBefore(start()) && at.isBefore(end());
  }

  boolean uses(Slot slot) {
    return slots.contains(slot);
  }
}
