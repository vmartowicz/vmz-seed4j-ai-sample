package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.Ticket;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketType;

public record JavaTicket(String number, String holderName, String holderEmail, boolean speaker, boolean sponsor, boolean valid) {
  static JavaTicket from(Ticket ticket) {
    return new JavaTicket(
      ticket.number().get(),
      ticket.holder().name(),
      ticket.holder().email().get(),
      ticket.type() == TicketType.SPEAKER,
      ticket.type() == TicketType.SPONSOR,
      ticket.isValid()
    );
  }
}
