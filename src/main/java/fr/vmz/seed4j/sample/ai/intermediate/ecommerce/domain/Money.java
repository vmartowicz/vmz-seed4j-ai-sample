package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {
  public static final Money ZERO = new Money(BigDecimal.ZERO);

  public Money {
    Assert.field("amount", amount).min(0);

    amount = amount.setScale(Price.CENTS, RoundingMode.HALF_UP);
  }

  public Money plus(Money other) {
    return new Money(amount.add(other.amount()));
  }

  public BigDecimal get() {
    return amount();
  }
}
