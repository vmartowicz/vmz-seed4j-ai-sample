package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import java.math.BigDecimal;

public enum Plan {
  ESSENTIAL(new BigDecimal("29.90")),
  PREMIUM(new BigDecimal("49.90"));

  private final BigDecimal price;

  Plan(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal price() {
    return price;
  }
}
