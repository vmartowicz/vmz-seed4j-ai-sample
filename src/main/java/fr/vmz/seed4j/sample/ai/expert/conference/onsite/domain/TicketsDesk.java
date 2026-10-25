package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import java.util.Optional;

public interface TicketsDesk {
  Optional<TicketView> ticket(String number);
}
