package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;

class WorkshopRoomException extends ProgramRuleViolationException {

  WorkshopRoomException(Room room) {
    super("Workshops take place in FOURVIERE and only there, not in " + room);
  }
}
