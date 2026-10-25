package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.MeetingRoomRuleViolationException;

class DuplicatedRoomNameException extends MeetingRoomRuleViolationException {

  DuplicatedRoomNameException(RoomName name) {
    super("A room is already named " + name.get());
  }
}
