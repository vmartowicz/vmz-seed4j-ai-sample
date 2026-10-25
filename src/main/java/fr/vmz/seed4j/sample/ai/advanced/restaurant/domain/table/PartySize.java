package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record PartySize(int people) {
  private static final int DEPOSIT_FROM = 6;

  public PartySize {
    Assert.field("people", people).min(2).max(8);
  }

  public boolean requiresDeposit() {
    return people >= DEPOSIT_FROM;
  }

  public boolean isCouple() {
    return people == 2;
  }

  public int get() {
    return people();
  }
}
