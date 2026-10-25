package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.MeetingRoomRuleViolationException;

class RoomAlreadyBookedException extends MeetingRoomRuleViolationException {

  RoomAlreadyBookedException(HourSlot slot) {
    super("The room is already booked at " + slot.start());
  }
}
