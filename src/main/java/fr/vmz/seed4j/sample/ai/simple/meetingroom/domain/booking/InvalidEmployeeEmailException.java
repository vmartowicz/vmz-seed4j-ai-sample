package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.InvalidMeetingRoomValueException;

class InvalidEmployeeEmailException extends InvalidMeetingRoomValueException {

  InvalidEmployeeEmailException(String email) {
    super("Invalid email: " + email);
  }
}
