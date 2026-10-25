package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

class TicketNotValidException extends TicketingRuleViolationException {

  TicketNotValidException(TicketNumber ticket, TicketStatus status) {
    super("Ticket " + ticket.get() + " is " + status);
  }
}
