package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

public enum TicketType {
  EARLY_BIRD(true),
  STANDARD(true),
  STUDENT(true),
  SPEAKER(false),
  SPONSOR(false);

  private final boolean sold;

  TicketType(boolean sold) {
    this.sold = sold;
  }

  public boolean isSold() {
    return sold;
  }
}
