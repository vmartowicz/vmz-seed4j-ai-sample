package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record AccountId(UUID id) {
  public AccountId {
    Assert.notNull("id", id);
  }

  public static AccountId newId() {
    return new AccountId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
