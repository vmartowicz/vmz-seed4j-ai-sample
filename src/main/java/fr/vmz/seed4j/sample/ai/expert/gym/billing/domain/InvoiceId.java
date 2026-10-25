package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record InvoiceId(UUID id) {
  public InvoiceId {
    Assert.notNull("id", id);
  }

  public static InvoiceId newId() {
    return new InvoiceId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
