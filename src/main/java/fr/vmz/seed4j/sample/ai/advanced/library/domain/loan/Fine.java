package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Fine(BigDecimal amount) {
  public static final Fine NONE = new Fine(BigDecimal.ZERO);

  public Fine {
    Assert.field("amount", amount).min(0);

    amount = amount.setScale(2, RoundingMode.HALF_UP);
  }

  public static Fine forLateDays(long lateDays) {
    return new Fine(new BigDecimal("0.50").multiply(BigDecimal.valueOf(lateDays)));
  }

  public Fine plus(Fine other) {
    return new Fine(amount.add(other.amount()));
  }

  public boolean exceeds(Fine other) {
    return amount.compareTo(other.amount()) > 0;
  }

  public boolean isDue() {
    return exceeds(NONE);
  }

  public BigDecimal get() {
    return amount();
  }
}
