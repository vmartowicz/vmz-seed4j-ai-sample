package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;

public record Amount(BigDecimal amount) {
  static final int CENTS = 2;

  public Amount {
    Assert.field("amount", amount).strictlyPositive();

    if (amount.stripTrailingZeros().scale() > CENTS) {
      throw new InvalidAmountException(amount);
    }

    amount = amount.setScale(CENTS);
  }

  public BigDecimal get() {
    return amount();
  }
}
