package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public final class AccountsFixture {

  public static final Instant NOW = Instant.parse("2026-03-10T09:00:00Z");

  private AccountsFixture() {}

  public static HolderName jane() {
    return new HolderName("Jane Doe");
  }

  public static Account account() {
    return Account.open(jane(), NOW);
  }

  public static Account accountWith(String balance) {
    var account = account();
    account.receiveTransfer(amount(balance), Optional.empty(), NOW);

    return account;
  }

  public static Amount amount(String value) {
    return new Amount(new BigDecimal(value));
  }

  public static Balance balance(String value) {
    return new Balance(new BigDecimal(value));
  }

  public static Optional<Label> label(String label) {
    return Optional.of(new Label(label));
  }
}
