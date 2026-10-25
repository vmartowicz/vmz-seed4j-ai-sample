package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.secondary.ticketing;

import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorTickets;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary.JavaTickets;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class SponsoringTickets implements SponsorTickets {

  private final JavaTickets tickets;

  SponsoringTickets(@Lazy JavaTickets tickets) {
    this.tickets = tickets;
  }

  @Override
  public List<String> issue(String contactName, Email contactEmail, int count) {
    return tickets.issueSponsorTickets(contactName, contactEmail.get(), count);
  }
}
