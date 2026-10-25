package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain;

public abstract class InvalidMeetingRoomValueException extends RuntimeException {

  protected InvalidMeetingRoomValueException(String message) {
    super(message);
  }
}
