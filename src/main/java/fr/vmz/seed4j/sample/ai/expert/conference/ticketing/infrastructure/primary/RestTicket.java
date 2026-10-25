package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.Ticket;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketStatus;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketType;
import java.math.BigDecimal;

record RestTicket(String number, TicketType type, String holderName, String holderEmail, BigDecimal price, TicketStatus status) {
  static RestTicket from(Ticket ticket) {
    return new RestTicket(
      ticket.number().get(),
      ticket.type(),
      ticket.holder().name(),
      ticket.holder().email().get(),
      ticket.price(),
      ticket.status()
    );
  }
}
