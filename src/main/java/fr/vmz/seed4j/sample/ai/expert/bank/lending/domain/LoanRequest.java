package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;

public record LoanRequest(CustomerId borrower, BigDecimal amount, int months) {
  public LoanRequest {
    Assert.notNull("borrower", borrower);
    Assert.field("amount", amount).min(1000).max(75000);
    Assert.field("months", months).min(12).max(84);
  }
}
