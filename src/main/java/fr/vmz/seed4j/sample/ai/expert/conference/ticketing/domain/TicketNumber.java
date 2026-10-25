package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Locale;
import java.util.UUID;

public record TicketNumber(String number) {
  public TicketNumber {
    Assert.field("ticket", number).notBlank().maxLength(20);

    number = number.trim().toUpperCase(Locale.ROOT);
  }

  public static TicketNumber generate() {
    return new TicketNumber("RC-" + UUID.randomUUID().toString().substring(0, 8));
  }

  public String get() {
    return number();
  }
}
