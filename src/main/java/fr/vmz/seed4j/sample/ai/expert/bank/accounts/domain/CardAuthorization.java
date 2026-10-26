package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import java.util.Optional;

public record CardAuthorization(Optional<CardRefusal> refusal) {
  static CardAuthorization of(Optional<CardRefusal> refusal) {
    return new CardAuthorization(refusal);
  }

  public boolean accepted() {
    return refusal.isEmpty();
  }
}
