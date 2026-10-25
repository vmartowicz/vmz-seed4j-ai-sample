package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.application.TicketingApplicationService;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.Holder;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.Ticket;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketNumber;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaTickets {

  private final TicketingApplicationService ticketing;

  public JavaTickets(TicketingApplicationService ticketing) {
    this.ticketing = ticketing;
  }

  public String issueSpeakerTicket(String name, String email) {
    return ticketing.issueSpeakerTicket(new Holder(name, new Email(email))).number().get();
  }

  public void revokeSpeakerTicket(String email) {
    ticketing.revokeSpeakerTicket(new Email(email));
  }

  public List<String> issueSponsorTickets(String contactName, String contactEmail, int count) {
    return ticketing
      .issueSponsorTickets(new Holder(contactName, new Email(contactEmail)), count)
      .stream()
      .map(Ticket::number)
      .map(TicketNumber::get)
      .toList();
  }

  public Optional<JavaTicket> ticket(String number) {
    return ticketing.findTicket(new TicketNumber(number)).map(JavaTicket::from);
  }

  public boolean isRegistered(UUID workshop, String ticket) {
    return ticketing.isRegistered(workshop, new TicketNumber(ticket));
  }
}
