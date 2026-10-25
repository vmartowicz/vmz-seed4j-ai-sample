package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.OrderToPlace;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.List;
import org.jspecify.annotations.Nullable;

record RestOrderToPlace(@Nullable String buyer, @Nullable List<RestAttendee> attendees) {
  OrderToPlace toDomain() {
    return new OrderToPlace(new Email(buyer), attendees == null ? List.of() : attendees.stream().map(RestAttendee::toDomain).toList());
  }
}
