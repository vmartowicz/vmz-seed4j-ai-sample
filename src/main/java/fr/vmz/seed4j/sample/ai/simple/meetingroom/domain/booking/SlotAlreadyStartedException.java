package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.MeetingRoomRuleViolationException;

class SlotAlreadyStartedException extends MeetingRoomRuleViolationException {

  SlotAlreadyStartedException(HourSlot slot) {
    super("Can't book a slot that has already started: " + slot.start());
  }
}
