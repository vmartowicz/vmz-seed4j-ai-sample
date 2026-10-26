package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record LoanId(UUID id) {
  public LoanId {
    Assert.notNull("loanId", id);
  }

  public static LoanId newId() {
    return new LoanId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
