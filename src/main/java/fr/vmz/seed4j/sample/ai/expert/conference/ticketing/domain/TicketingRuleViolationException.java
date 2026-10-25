package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

public abstract class TicketingRuleViolationException extends RuntimeException {

  protected TicketingRuleViolationException(String message) {
    super(message);
  }
}
