package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

class RefundPeriodOverException extends TicketingRuleViolationException {

  RefundPeriodOverException(TicketNumber ticket) {
    super("Tickets are refunded until 30 days before the conference, too late for ticket " + ticket.get());
  }
}
