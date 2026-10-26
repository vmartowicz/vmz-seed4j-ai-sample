package fr.vmz.seed4j.sample.ai.expert.bank.accounts.application;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.AccountType;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.AccountsDesk;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.BankAccount;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.BankAccountsRepository;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.Card;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardAuthorization;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardId;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardType;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardsRepository;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CustomerDirectory;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RetailAccountsApplicationService {

  private final AccountsDesk desk;
  private final Clock clock;

  public RetailAccountsApplicationService(BankAccountsRepository accounts, CardsRepository cards, CustomerDirectory customers, Clock clock) {
    desk = new AccountsDesk(accounts, cards, customers);
    this.clock = clock;
  }

  public BankAccount open(AccountType type, List<CustomerId> holders) {
    return desk.open(type, holders, LocalDate.now(clock));
  }

  public BankAccount account(Iban iban) {
    return desk.account(iban);
  }

  public BankAccount deposit(Iban iban, BigDecimal amount) {
    return desk.deposit(iban, amount, now());
  }

  public void freezeAccountsOf(CustomerId customer) {
    desk.freezeAccountsOf(customer);
  }

  public void unfreezeAccountsOf(CustomerId customer) {
    desk.unfreezeAccountsOf(customer);
  }

  public BankAccount freezeForSanctions(Iban iban) {
    return desk.freezeForSanctions(iban);
  }

  public Card issueCard(Iban iban, CardType type) {
    return desk.issueCard(iban, type);
  }

  public Card card(CardId card) {
    return desk.card(card);
  }

  public CardAuthorization pay(CardId card, BigDecimal amount, String country) {
    return desk.pay(card, amount, country, now());
  }

  public CardAuthorization withdraw(CardId card, BigDecimal amount) {
    return desk.withdraw(card, amount, now());
  }

  public Card wrongPin(CardId card) {
    return desk.wrongPin(card);
  }

  public Card declareLostOrStolen(CardId card) {
    return desk.declareLostOrStolen(card);
  }

  public BankAccount debit(Iban iban, BigDecimal amount, String label) {
    return desk.debit(iban, amount, label, now());
  }

  public BankAccount credit(Iban iban, BigDecimal amount, String label) {
    return desk.credit(iban, amount, label, now());
  }

  public BankAccount debitBeyondOverdraft(Iban iban, BigDecimal amount, String label) {
    return desk.debitBeyondOverdraft(iban, amount, label, now());
  }

  public BankAccount collectForBank(Iban iban, BigDecimal amount, String label) {
    return desk.collectForBank(iban, amount, label, now());
  }

  public BankAccount chargeFee(Iban iban, BigDecimal amount, String label) {
    return desk.chargeFee(iban, amount, label, now());
  }

  public Optional<Iban> currentAccountOf(CustomerId customer) {
    return desk.currentAccountOf(customer);
  }

  public boolean wasInUnauthorizedOverdraftSince(CustomerId customer, LocalDate date) {
    return desk.wasInUnauthorizedOverdraftSince(customer, date);
  }

  public boolean exists(Iban iban) {
    return desk.exists(iban);
  }

  public void accrueInterest() {
    desk.accrueInterest();
  }

  public void closeMonth() {
    desk.closeMonth(now());
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
