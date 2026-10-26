package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record FinalInvoiceId(UUID id) {
  public FinalInvoiceId {
    Assert.notNull("invoiceId", id);
  }

  public static FinalInvoiceId newId() {
    return new FinalInvoiceId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
