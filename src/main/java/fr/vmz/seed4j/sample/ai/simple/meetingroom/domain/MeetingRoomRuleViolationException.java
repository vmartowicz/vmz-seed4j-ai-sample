package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain;

public abstract class MeetingRoomRuleViolationException extends RuntimeException {

  protected MeetingRoomRuleViolationException(String message) {
    super(message);
  }
}
