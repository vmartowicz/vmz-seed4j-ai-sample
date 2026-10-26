package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record TransferId(UUID id) {
  public TransferId {
    Assert.notNull("transferId", id);
  }

  public static TransferId newId() {
    return new TransferId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
