package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.secondary.ticket;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.Ticket;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketNumber;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketsRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryTicketsRepository implements TicketsRepository {

  private final Map<TicketNumber, Ticket> tickets = new ConcurrentHashMap<>();

  @Override
  public void save(Ticket ticket) {
    tickets.put(ticket.number(), ticket);
  }

  @Override
  public Optional<Ticket> get(TicketNumber ticket) {
    return Optional.ofNullable(tickets.get(ticket));
  }

  @Override
  public List<Ticket> all() {
    return List.copyOf(tickets.values());
  }
}
