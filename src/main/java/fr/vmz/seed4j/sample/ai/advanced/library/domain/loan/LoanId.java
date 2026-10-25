package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record LoanId(UUID id) {
  public LoanId {
    Assert.notNull("id", id);
  }

  public static LoanId newId() {
    return new LoanId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
