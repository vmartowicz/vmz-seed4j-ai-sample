package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record ContractId(UUID id) {
  public ContractId {
    Assert.notNull("contractId", id);
  }

  public static ContractId newId() {
    return new ContractId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
