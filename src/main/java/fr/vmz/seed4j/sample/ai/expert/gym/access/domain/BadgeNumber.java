package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Locale;
import java.util.UUID;

public record BadgeNumber(String number) {
  public BadgeNumber {
    Assert.field("badge", number).notBlank().maxLength(20);

    number = number.trim().toUpperCase(Locale.ROOT);
  }

  public static BadgeNumber generate() {
    return new BadgeNumber("FC-" + UUID.randomUUID().toString().substring(0, 8));
  }

  public String get() {
    return number();
  }
}
