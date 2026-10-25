package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

class TicketNotValidForBadgeException extends OnsiteRuleViolationException {

  TicketNotValidForBadgeException(String ticket) {
    super("Ticket " + ticket + " is no longer valid");
  }
}
