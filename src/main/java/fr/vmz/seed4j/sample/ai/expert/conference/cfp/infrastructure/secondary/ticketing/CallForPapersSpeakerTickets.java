package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.secondary.ticketing;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.SpeakerProfile;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.SpeakerTickets;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary.JavaTickets;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class CallForPapersSpeakerTickets implements SpeakerTickets {

  private final JavaTickets tickets;

  // Lazy: ticketing checks workshops in the call for papers, the contexts depend on each other
  CallForPapersSpeakerTickets(@Lazy JavaTickets tickets) {
    this.tickets = tickets;
  }

  @Override
  public void issueFor(SpeakerProfile speaker) {
    tickets.issueSpeakerTicket(speaker.name(), speaker.email().get());
  }

  @Override
  public void revokeFor(Email speaker) {
    tickets.revokeSpeakerTicket(speaker.get());
  }
}
