package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {
  public Money {
    Assert.field("amount", amount).min(0);

    amount = amount.setScale(2, RoundingMode.HALF_UP);
  }

  public Money plus(Money other) {
    return new Money(amount.add(other.amount()));
  }

  public Money prorata(int days, int periodDays) {
    return new Money(amount.multiply(BigDecimal.valueOf(days)).divide(BigDecimal.valueOf(periodDays), 2, RoundingMode.HALF_UP));
  }

  public BigDecimal get() {
    return amount();
  }
}
