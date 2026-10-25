package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketNumber;
import org.jspecify.annotations.Nullable;

record RestRegistrationRequest(@Nullable String ticket) {
  TicketNumber toDomain() {
    return new TicketNumber(ticket);
  }
}
