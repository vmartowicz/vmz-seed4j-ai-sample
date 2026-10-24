package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Balance(BigDecimal balance) {
  public static final Balance ZERO = new Balance(BigDecimal.ZERO);

  public Balance {
    Assert.field("balance", balance).min(0);

    balance = balance.setScale(Amount.CENTS, RoundingMode.HALF_UP);
  }

  public Balance plus(Amount amount) {
    return new Balance(balance.add(amount.get()));
  }

  public Balance minus(Amount amount) {
    return new Balance(balance.subtract(amount.get()));
  }

  public boolean covers(Amount amount) {
    return balance.compareTo(amount.get()) >= 0;
  }

  public BigDecimal get() {
    return balance();
  }
}
