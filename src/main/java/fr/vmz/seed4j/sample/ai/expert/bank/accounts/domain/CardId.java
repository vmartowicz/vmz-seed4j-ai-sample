package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record CardId(UUID id) {
  public CardId {
    Assert.notNull("cardId", id);
  }

  public static CardId newId() {
    return new CardId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
