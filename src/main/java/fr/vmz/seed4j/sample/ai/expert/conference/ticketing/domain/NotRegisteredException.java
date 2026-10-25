package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import java.util.UUID;

class NotRegisteredException extends TicketingRuleViolationException {

  NotRegisteredException(TicketNumber ticket, UUID workshop) {
    super("Ticket " + ticket.get() + " is not registered to workshop " + workshop);
  }
}
