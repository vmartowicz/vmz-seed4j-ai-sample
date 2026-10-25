package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.PlacedOrder;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

record RestOrder(UUID id, String buyer, BigDecimal total, List<RestTicket> tickets) {
  static RestOrder from(PlacedOrder order) {
    return new RestOrder(order.id(), order.buyer().get(), order.total(), order.tickets().stream().map(RestTicket::from).toList());
  }
}
