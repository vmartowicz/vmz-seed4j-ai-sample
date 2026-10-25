package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

class FreeTicketNotRefundableException extends TicketingRuleViolationException {

  FreeTicketNotRefundableException(TicketNumber ticket) {
    super("Free ticket " + ticket.get() + " can not be refunded");
  }
}
