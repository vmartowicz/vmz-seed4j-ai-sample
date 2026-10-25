package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

public abstract class UnknownTicketingElementException extends RuntimeException {

  protected UnknownTicketingElementException(String message) {
    super(message);
  }
}
