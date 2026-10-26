package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import java.math.BigDecimal;

public enum CardType {
  CLASSIC(new BigDecimal("1500"), new BigDecimal("500")),
  GOLD(new BigDecimal("5000"), new BigDecimal("1000"));

  private final BigDecimal paymentLimit;
  private final BigDecimal withdrawalLimit;

  CardType(BigDecimal paymentLimit, BigDecimal withdrawalLimit) {
    this.paymentLimit = paymentLimit;
    this.withdrawalLimit = withdrawalLimit;
  }

  BigDecimal paymentLimit() {
    return paymentLimit;
  }

  BigDecimal withdrawalLimit() {
    return withdrawalLimit;
  }
}
