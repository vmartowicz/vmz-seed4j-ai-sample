package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.List;

public record OrderToPlace(Email buyer, List<AttendeeToRegister> attendees) {
  public OrderToPlace {
    Assert.notNull("buyer", buyer);
    Assert.field("attendees", attendees).notEmpty().noNullElement();

    attendees = List.copyOf(attendees);
  }
}
