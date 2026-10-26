package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;

public record Tariff(boolean specialist, BigDecimal feeOverrun) {
  private static final BigDecimal GENERAL_PRACTITIONER_FEE = new BigDecimal("30");
  private static final BigDecimal SPECIALIST_FEE = new BigDecimal("50");

  public Tariff {
    Assert.field("feeOverrun", feeOverrun).min(BigDecimal.ZERO);
  }

  BigDecimal baseFee() {
    return specialist ? SPECIALIST_FEE : GENERAL_PRACTITIONER_FEE;
  }
}
