package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.UnknownMeetingRoomElementException;

public class UnknownRoomException extends UnknownMeetingRoomElementException {

  UnknownRoomException(RoomId room) {
    super("Unknown room " + room.get());
  }
}
