package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.secondary.ticket;

import static fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketNumber;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryTicketsRepositoryTest {

  private final InMemoryTicketsRepository tickets = new InMemoryTicketsRepository();

  @Test
  void shouldGetSavedTickets() {
    var ticket = standard();

    tickets.save(ticket);

    assertThat(tickets.get(ticket.number())).containsSame(ticket);
    assertThat(tickets.get(TicketNumber.generate())).isEmpty();
    assertThat(tickets.all()).containsExactly(ticket);
  }
}
