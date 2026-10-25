package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

class UnknownTicketException extends UnknownTicketingElementException {

  UnknownTicketException(TicketNumber ticket) {
    super("Unknown ticket " + ticket.get());
  }
}
