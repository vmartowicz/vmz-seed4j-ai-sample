package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.Instant;
import java.util.Optional;

public record Operation(OperationType type, Amount amount, Optional<Label> label, Instant date, Balance balanceAfter) {
  public Operation {
    Assert.notNull("type", type);
    Assert.notNull("amount", amount);
    Assert.notNull("label", label);
    Assert.notNull("date", date);
    Assert.notNull("balanceAfter", balanceAfter);
  }
}
