package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

class NameChangeClosedException extends TicketingRuleViolationException {

  NameChangeClosedException(TicketNumber ticket) {
    super("Names can be changed until 7 days before the conference, too late for ticket " + ticket.get());
  }
}
