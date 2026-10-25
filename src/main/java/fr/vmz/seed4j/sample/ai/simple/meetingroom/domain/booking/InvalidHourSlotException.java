package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.InvalidMeetingRoomValueException;
import java.time.LocalDateTime;

class InvalidHourSlotException extends InvalidMeetingRoomValueException {

  InvalidHourSlotException(LocalDateTime start) {
    super("No slot starts at " + start + ": slots start on the hour, Monday to Friday from 08:00 to 18:00");
  }
}
