package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class AccountsDesk {

  private final BankAccountsRepository accounts;
  private final CardsRepository cards;
  private final CustomerDirectory customers;

  public AccountsDesk(BankAccountsRepository accounts, CardsRepository cards, CustomerDirectory customers) {
    this.accounts = accounts;
    this.cards = cards;
    this.customers = customers;
  }

  public BankAccount open(AccountType type, List<CustomerId> holders, LocalDate today) {
    var resolved = holders
      .stream()
      .map(holder -> customers.holder(holder).orElseThrow(() -> new UnknownHolderException(holder)))
      .toList();

    var account = new AccountToOpen(type, resolved).open(today);
    accounts.save(account);

    return account;
  }

  public BankAccount deposit(Iban iban, BigDecimal amount, LocalDateTime at) {
    return update(iban, account -> account.credit(amount, "Cash deposit", at));
  }

  public void freezeAccountsOf(CustomerId customer) {
    forAccountsOf(customer, account -> account.freezeForReview(customer));
  }

  public void unfreezeAccountsOf(CustomerId customer) {
    forAccountsOf(customer, account -> account.unfreezeForReview(customer));
  }

  public BankAccount freezeForSanctions(Iban iban) {
    return update(iban, BankAccount::freezeForSanctions);
  }

  public Card issueCard(Iban iban, CardType type) {
    var card = Card.issue(accounts.account(iban), type);

    cards.save(card);

    return card;
  }

  public CardAuthorization pay(CardId id, BigDecimal amount, String country, LocalDateTime at) {
    var card = cards.card(id);
    var refusal = card.authorizePayment(amount, country, at).or(() -> debitCard(card, amount, "Card payment " + country, at));
    if (refusal.isEmpty()) {
      card.recordPayment(amount, country, at);
    }

    cards.save(card);
    return CardAuthorization.of(refusal);
  }

  public CardAuthorization withdraw(CardId id, BigDecimal amount, LocalDateTime at) {
    var card = cards.card(id);
    var refusal = card.authorizeWithdrawal(amount, at).or(() -> debitCard(card, amount, "Cash withdrawal", at));
    if (refusal.isEmpty()) {
      card.recordWithdrawal(amount, at);
    }

    cards.save(card);
    return CardAuthorization.of(refusal);
  }

  public Card wrongPin(CardId id) {
    var card = cards.card(id);

    card.wrongPin();
    cards.save(card);

    return card;
  }

  public Card declareLostOrStolen(CardId id) {
    var card = cards.card(id);

    card.declareLostOrStolen();
    cards.save(card);

    return card;
  }

  public void accrueInterest() {
    accounts
      .all()
      .forEach(account -> {
        account.accrueInterest();
        accounts.save(account);
      });
  }

  public void closeMonth(LocalDateTime at) {
    accounts
      .all()
      .forEach(account -> {
        account.closeMonth(at, everyHolderUnder25(account, at.toLocalDate()));
        accounts.save(account);
      });
  }

  public BankAccount debit(Iban iban, BigDecimal amount, String label, LocalDateTime at) {
    return update(iban, account -> account.debit(amount, label, at));
  }

  public BankAccount credit(Iban iban, BigDecimal amount, String label, LocalDateTime at) {
    return update(iban, account -> account.credit(amount, label, at));
  }

  public BankAccount debitBeyondOverdraft(Iban iban, BigDecimal amount, String label, LocalDateTime at) {
    return update(iban, account -> account.debitBeyondOverdraft(amount, label, at));
  }

  public BankAccount collectForBank(Iban iban, BigDecimal amount, String label, LocalDateTime at) {
    return update(iban, account -> account.collectForBank(amount, label, at));
  }

  public BankAccount chargeFee(Iban iban, BigDecimal amount, String label, LocalDateTime at) {
    return update(iban, account -> account.chargeFee(amount, label, at));
  }

  public Optional<Iban> currentAccountOf(CustomerId customer) {
    return accounts
      .ofHolder(customer)
      .stream()
      .filter(account -> account.type() == AccountType.CURRENT)
      .map(BankAccount::iban)
      .findFirst();
  }

  public boolean wasInUnauthorizedOverdraftSince(CustomerId customer, LocalDate date) {
    return accounts
      .ofHolder(customer)
      .stream()
      .anyMatch(account -> account.wasInUnauthorizedOverdraftSince(date));
  }

  public boolean exists(Iban iban) {
    return accounts.get(iban).isPresent();
  }

  public BankAccount account(Iban iban) {
    return accounts.account(iban);
  }

  public Card card(CardId id) {
    return cards.card(id);
  }

  private Optional<CardRefusal> debitCard(Card card, BigDecimal amount, String label, LocalDateTime at) {
    var account = accounts.account(card.account());
    if (account.isFrozen()) {
      return Optional.of(CardRefusal.ACCOUNT_FROZEN);
    }

    account.debitBeyondOverdraft(amount, label, at);
    accounts.save(account);
    return Optional.empty();
  }

  private boolean everyHolderUnder25(BankAccount account, LocalDate date) {
    return account
      .holders()
      .stream()
      .allMatch(holder -> customers.holder(holder).filter(found -> found.isUnder25On(date)).isPresent());
  }

  private BankAccount update(Iban iban, Consumer<BankAccount> change) {
    var account = accounts.account(iban);

    change.accept(account);
    accounts.save(account);

    return account;
  }

  private void forAccountsOf(CustomerId customer, Consumer<BankAccount> change) {
    accounts
      .ofHolder(customer)
      .forEach(account -> {
        change.accept(account);
        accounts.save(account);
      });
  }
}
