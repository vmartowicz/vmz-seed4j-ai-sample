package fr.vmz.seed4j.sample.ai.advanced.shop.domain;

import java.math.BigDecimal;

public final class MoneyFixture {

  private MoneyFixture() {}

  public static Money money(String amount) {
    return new Money(new BigDecimal(amount));
  }
}
