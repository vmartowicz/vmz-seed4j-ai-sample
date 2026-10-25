package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

class SoldOutException extends TicketingRuleViolationException {

  SoldOutException(int capacity) {
    super("The conference is sold out: " + capacity + " tickets");
  }
}
