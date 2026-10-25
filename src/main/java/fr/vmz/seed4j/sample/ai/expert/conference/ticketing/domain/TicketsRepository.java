package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import java.util.List;
import java.util.Optional;

public interface TicketsRepository {
  void save(Ticket ticket);

  Optional<Ticket> get(TicketNumber ticket);

  List<Ticket> all();

  default Ticket ticket(TicketNumber ticket) {
    return get(ticket).orElseThrow(() -> new UnknownTicketException(ticket));
  }
}
