package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Account {

  private final AccountId id;
  private final HolderName holder;
  private final Instant openedAt;
  private final List<Operation> operations = new ArrayList<>();
  private Balance balance = Balance.ZERO;

  private Account(AccountId id, HolderName holder, Instant openedAt) {
    Assert.notNull("holder", holder);
    Assert.notNull("openedAt", openedAt);

    this.id = id;
    this.holder = holder;
    this.openedAt = openedAt;
  }

  public static Account open(HolderName holder, Instant now) {
    return new Account(AccountId.newId(), holder, now);
  }

  public Operation receiveTransfer(Amount amount, Optional<Label> label, Instant now) {
    Assert.notNull("amount", amount);

    return record(OperationType.TRANSFER_RECEIVED, amount, label, now, balance.plus(amount));
  }

  public Operation pay(Amount amount, Optional<Label> label, Instant now) {
    Assert.notNull("amount", amount);

    if (!balance.covers(amount)) {
      throw new InsufficientBalanceException(id, amount);
    }

    return record(OperationType.PAYMENT, amount, label, now, balance.minus(amount));
  }

  private Operation record(OperationType type, Amount amount, Optional<Label> label, Instant now, Balance newBalance) {
    var operation = new Operation(type, amount, label, now, newBalance);

    operations.add(operation);
    balance = newBalance;

    return operation;
  }

  public AccountId id() {
    return id;
  }

  public HolderName holder() {
    return holder;
  }

  public Instant openedAt() {
    return openedAt;
  }

  public Balance balance() {
    return balance;
  }

  public List<Operation> history() {
    return List.copyOf(operations.reversed());
  }
}
