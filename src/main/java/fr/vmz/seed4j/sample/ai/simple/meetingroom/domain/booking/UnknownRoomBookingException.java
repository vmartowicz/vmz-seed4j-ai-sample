package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.UnknownMeetingRoomElementException;

class UnknownRoomBookingException extends UnknownMeetingRoomElementException {

  UnknownRoomBookingException(RoomBookingId booking) {
    super("Unknown booking " + booking.get());
  }
}
