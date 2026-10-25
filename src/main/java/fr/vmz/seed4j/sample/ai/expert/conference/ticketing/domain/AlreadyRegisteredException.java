package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import java.util.UUID;

class AlreadyRegisteredException extends TicketingRuleViolationException {

  AlreadyRegisteredException(TicketNumber ticket, UUID workshop) {
    super("Ticket " + ticket.get() + " is already registered to workshop " + workshop);
  }
}
