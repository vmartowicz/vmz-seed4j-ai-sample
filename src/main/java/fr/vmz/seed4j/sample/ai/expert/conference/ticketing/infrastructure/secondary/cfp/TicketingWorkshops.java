package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.secondary.cfp;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary.JavaConfirmedTalks;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.Workshops;
import java.util.UUID;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class TicketingWorkshops implements Workshops {

  private final JavaConfirmedTalks talks;

  // Lazy: the call for papers issues speaker tickets, the contexts depend on each other
  TicketingWorkshops(@Lazy JavaConfirmedTalks talks) {
    this.talks = talks;
  }

  @Override
  public boolean exists(UUID workshop) {
    return talks.isConfirmedWorkshop(workshop);
  }
}
