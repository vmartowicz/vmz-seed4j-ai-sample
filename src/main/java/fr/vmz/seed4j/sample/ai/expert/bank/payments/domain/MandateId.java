package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record MandateId(UUID id) {
  public MandateId {
    Assert.notNull("mandateId", id);
  }

  public static MandateId newId() {
    return new MandateId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
