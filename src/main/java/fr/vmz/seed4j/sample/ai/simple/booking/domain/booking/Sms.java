package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Sms(MobilePhone to, String text) {
  public Sms {
    Assert.notNull("to", to);
    Assert.notBlank("text", text);
  }
}
