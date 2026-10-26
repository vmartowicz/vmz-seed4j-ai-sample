package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record PayeeId(UUID id) {
  public PayeeId {
    Assert.notNull("payeeId", id);
  }

  public static PayeeId newId() {
    return new PayeeId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
