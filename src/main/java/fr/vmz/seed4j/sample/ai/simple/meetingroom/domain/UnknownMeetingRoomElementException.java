package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain;

public abstract class UnknownMeetingRoomElementException extends RuntimeException {

  protected UnknownMeetingRoomElementException(String message) {
    super(message);
  }
}
