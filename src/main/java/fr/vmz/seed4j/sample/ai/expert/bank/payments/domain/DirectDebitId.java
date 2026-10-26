package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record DirectDebitId(UUID id) {
  public DirectDebitId {
    Assert.notNull("directDebitId", id);
  }

  public static DirectDebitId newId() {
    return new DirectDebitId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
