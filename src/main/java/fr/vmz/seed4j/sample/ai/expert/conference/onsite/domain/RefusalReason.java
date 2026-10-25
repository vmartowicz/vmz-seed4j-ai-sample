package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

public enum RefusalReason {
  ROOM_FULL("The room is full, please follow the live stream in the hall"),
  NOT_REGISTERED("This workshop is open to registered attendees only");

  private final String message;

  RefusalReason(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }
}
