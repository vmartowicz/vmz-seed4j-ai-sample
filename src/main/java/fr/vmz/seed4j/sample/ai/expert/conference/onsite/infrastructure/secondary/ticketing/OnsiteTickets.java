package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.secondary.ticketing;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.BadgeType;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.TicketView;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.TicketsDesk;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.WorkshopRegistrations;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary.JavaTicket;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary.JavaTickets;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class OnsiteTickets implements TicketsDesk, WorkshopRegistrations {

  private final JavaTickets tickets;

  OnsiteTickets(@Lazy JavaTickets tickets) {
    this.tickets = tickets;
  }

  @Override
  public Optional<TicketView> ticket(String number) {
    return tickets
      .ticket(number)
      .map(ticket -> new TicketView(ticket.number(), ticket.holderName(), new Email(ticket.holderEmail()), typeOf(ticket), ticket.valid()));
  }

  @Override
  public boolean isRegistered(UUID workshop, String ticket) {
    return tickets.isRegistered(workshop, ticket);
  }

  private static BadgeType typeOf(JavaTicket ticket) {
    if (ticket.speaker()) {
      return BadgeType.SPEAKER;
    }

    return ticket.sponsor() ? BadgeType.SPONSOR : BadgeType.ATTENDEE;
  }
}
