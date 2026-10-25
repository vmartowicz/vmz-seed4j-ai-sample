package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PlacedOrder(UUID id, Email buyer, List<Ticket> tickets) {
  public BigDecimal total() {
    return tickets.stream().map(Ticket::price).reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
