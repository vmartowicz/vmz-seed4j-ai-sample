package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.MeetingRoomRuleViolationException;

class RoomBookingAlreadyCancelledException extends MeetingRoomRuleViolationException {

  RoomBookingAlreadyCancelledException(RoomBookingId booking) {
    super("This booking is already cancelled: " + booking.get());
  }
}
