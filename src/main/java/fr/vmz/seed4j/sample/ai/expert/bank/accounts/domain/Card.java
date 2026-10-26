package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Card {

  private static final int MAX_WRONG_PINS = 3;
  private static final int PAYMENT_WINDOW_DAYS = 30;
  private static final int WITHDRAWAL_WINDOW_DAYS = 7;
  private static final String CASH = "CASH";

  private final CardId id;
  private final Iban account;
  private final CardType type;
  private final List<CardOperation> payments = new ArrayList<>();
  private final List<CardOperation> withdrawals = new ArrayList<>();
  private CardStatus status;
  private int wrongPins;

  private Card(Iban account, CardType type) {
    id = CardId.newId();
    this.account = account;
    this.type = type;
    status = CardStatus.ACTIVE;
  }

  public static Card issue(BankAccount account, CardType type) {
    if (account.type() == AccountType.SAVINGS) {
      throw new CardNotAllowedException("No card on a Livret A");
    }

    if (account.type() == AccountType.YOUTH && type == CardType.GOLD) {
      throw new CardNotAllowedException("No GOLD card on a youth account");
    }

    return new Card(account.iban(), type);
  }

  public Optional<CardRefusal> authorizePayment(BigDecimal amount, String country, LocalDateTime at) {
    if (status != CardStatus.ACTIVE) {
      return Optional.of(CardRefusal.CARD_NOT_ACTIVE);
    }

    var previous = payments.stream().filter(payment -> payment.at().isAfter(at.minusHours(1))).filter(payment -> !payment.country().equals(country));
    if (previous.findAny().isPresent()) {
      status = CardStatus.BLOCKED;
      return Optional.of(CardRefusal.FRAUD_SUSPECTED);
    }

    return withinLimit(payments, amount, at.minusDays(PAYMENT_WINDOW_DAYS), type.paymentLimit());
  }

  public Optional<CardRefusal> authorizeWithdrawal(BigDecimal amount, LocalDateTime at) {
    if (status != CardStatus.ACTIVE) {
      return Optional.of(CardRefusal.CARD_NOT_ACTIVE);
    }

    return withinLimit(withdrawals, amount, at.minusDays(WITHDRAWAL_WINDOW_DAYS), type.withdrawalLimit());
  }

  public void recordPayment(BigDecimal amount, String country, LocalDateTime at) {
    payments.add(new CardOperation(at, amount, country));
  }

  public void recordWithdrawal(BigDecimal amount, LocalDateTime at) {
    withdrawals.add(new CardOperation(at, amount, CASH));
  }

  public void wrongPin() {
    assertStatus(CardStatus.ACTIVE);

    wrongPins++;
    if (wrongPins == MAX_WRONG_PINS) {
      status = CardStatus.BLOCKED;
    }
  }

  public void declareLostOrStolen() {
    if (status == CardStatus.OPPOSED) {
      throw new CardNotActiveException(id, status);
    }

    status = CardStatus.OPPOSED;
  }

  private static Optional<CardRefusal> withinLimit(List<CardOperation> operations, BigDecimal amount, LocalDateTime since, BigDecimal limit) {
    var used = operations
      .stream()
      .filter(operation -> operation.at().isAfter(since))
      .map(CardOperation::amount)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    return used.add(amount).compareTo(limit) > 0 ? Optional.of(CardRefusal.LIMIT_REACHED) : Optional.empty();
  }

  private void assertStatus(CardStatus expected) {
    if (status != expected) {
      throw new CardNotActiveException(id, status);
    }
  }

  public CardId id() {
    return id;
  }

  public Iban account() {
    return account;
  }

  public CardType type() {
    return type;
  }

  public CardStatus status() {
    return status;
  }
}
