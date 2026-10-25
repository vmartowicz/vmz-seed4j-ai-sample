package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.MeetingRoomRuleViolationException;

class RoomBookingStartedException extends MeetingRoomRuleViolationException {

  RoomBookingStartedException(RoomBookingId booking) {
    super("This booking has already started: " + booking.get());
  }
}
