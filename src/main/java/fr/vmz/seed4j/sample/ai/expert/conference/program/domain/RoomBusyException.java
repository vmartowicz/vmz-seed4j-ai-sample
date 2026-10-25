package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;

class RoomBusyException extends ProgramRuleViolationException {

  RoomBusyException(Room room, Slot slot) {
    super("Room " + room + " is already used at " + slot.start());
  }
}
