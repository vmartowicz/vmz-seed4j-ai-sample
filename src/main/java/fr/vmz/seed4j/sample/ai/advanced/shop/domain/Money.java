package fr.vmz.seed4j.sample.ai.advanced.shop.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {
  public static final Money ZERO = new Money(BigDecimal.ZERO);

  public Money {
    Assert.field("amount", amount).min(0);

    amount = amount.setScale(2, RoundingMode.HALF_UP);
  }

  public Money plus(Money other) {
    return new Money(amount.add(other.amount()));
  }

  public Money minus(Money other) {
    return new Money(amount.subtract(other.amount()));
  }

  public Money times(int quantity) {
    return new Money(amount.multiply(BigDecimal.valueOf(quantity)));
  }

  public Money percent(int percentage) {
    return new Money(amount.multiply(BigDecimal.valueOf(percentage)).divide(BigDecimal.valueOf(100)));
  }

  public boolean isGreaterThan(Money other) {
    return amount.compareTo(other.amount()) > 0;
  }

  public BigDecimal get() {
    return amount();
  }
}
