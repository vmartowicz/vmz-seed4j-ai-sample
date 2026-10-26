package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BankAccount {

  private static final BigDecimal SAVINGS_CEILING = new BigDecimal("22950");
  private static final BigDecimal UNAUTHORIZED_FEE = new BigDecimal("8");
  private static final BigDecimal UNAUTHORIZED_FEES_CAP = new BigDecimal("80");
  private static final BigDecimal DAILY_OVERDRAFT_RATE = new BigDecimal("0.16").divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);
  private static final BigDecimal MONTHLY_FEE = new BigDecimal("2");

  private final Iban iban;
  private final AccountType type;
  private final List<CustomerId> holders;
  private final BigDecimal authorizedOverdraft;
  private final LocalDate openedOn;
  private final List<Entry> entries = new ArrayList<>();
  private final Set<CustomerId> overdueHolders = new HashSet<>();
  private final Map<YearMonth, BigDecimal> unauthorizedFees = new HashMap<>();
  private final List<LocalDate> unauthorizedOverdraftDays = new ArrayList<>();
  private BigDecimal balance = BigDecimal.ZERO;
  private BigDecimal accruedInterest = BigDecimal.ZERO;
  private boolean sanctioned;

  BankAccount(AccountType type, List<CustomerId> holders, BigDecimal authorizedOverdraft, LocalDate openedOn) {
    iban = Iban.generate();
    this.type = type;
    this.holders = holders;
    this.authorizedOverdraft = authorizedOverdraft;
    this.openedOn = openedOn;
  }

  public void credit(BigDecimal amount, String label, LocalDateTime at) {
    Assert.field("amount", amount).strictlyPositive();
    if (type == AccountType.SAVINGS && balance.add(amount).compareTo(SAVINGS_CEILING) > 0) {
      throw new SavingsCeilingException(iban);
    }

    move(amount, label, at);
  }

  public void debit(BigDecimal amount, String label, LocalDateTime at) {
    assertNotFrozen();
    collectForBank(amount, label, at);
  }

  public void debitBeyondOverdraft(BigDecimal amount, String label, LocalDateTime at) {
    Assert.field("amount", amount).strictlyPositive();
    assertNotFrozen();

    move(amount.negate(), label, at);
    if (isBeyondOverdraft()) {
      unauthorizedOverdraftDays.add(at.toLocalDate());
      var month = YearMonth.from(at);
      var fee = UNAUTHORIZED_FEE.min(UNAUTHORIZED_FEES_CAP.subtract(unauthorizedFees.getOrDefault(month, BigDecimal.ZERO)));
      if (fee.signum() > 0) {
        unauthorizedFees.merge(month, fee, BigDecimal::add);
        chargeFee(fee, "Unauthorized overdraft fee", at);
      }
    }
  }

  public void collectForBank(BigDecimal amount, String label, LocalDateTime at) {
    Assert.field("amount", amount).strictlyPositive();
    if (balance.subtract(amount).compareTo(authorizedOverdraft.negate()) < 0) {
      throw new InsufficientFundsException(iban);
    }

    move(amount.negate(), label, at);
  }

  public void chargeFee(BigDecimal amount, String label, LocalDateTime at) {
    move(amount.negate(), label, at);
  }

  public void accrueInterest() {
    if (balance.signum() < 0) {
      accruedInterest = accruedInterest.add(balance.negate().multiply(DAILY_OVERDRAFT_RATE));
    }
  }

  public void closeMonth(LocalDateTime at, boolean feeFree) {
    var agios = accruedInterest.setScale(2, RoundingMode.HALF_UP);
    if (agios.signum() > 0) {
      chargeFee(agios, "Overdraft interest", at);
    }

    if (!feeFree) {
      chargeFee(MONTHLY_FEE, "Account fee", at);
    }

    accruedInterest = BigDecimal.ZERO;
  }

  public void freezeForReview(CustomerId holder) {
    overdueHolders.add(holder);
  }

  public void unfreezeForReview(CustomerId holder) {
    overdueHolders.remove(holder);
  }

  public void freezeForSanctions() {
    sanctioned = true;
  }

  public boolean isFrozen() {
    return !freezes().isEmpty();
  }

  public Set<FreezeReason> freezes() {
    var freezes = EnumSet.noneOf(FreezeReason.class);
    if (!overdueHolders.isEmpty()) {
      freezes.add(FreezeReason.REVIEW_OVERDUE);
    }

    if (sanctioned) {
      freezes.add(FreezeReason.SANCTIONS);
    }

    return freezes;
  }

  public boolean wasInUnauthorizedOverdraftSince(LocalDate date) {
    return unauthorizedOverdraftDays.stream().anyMatch(day -> !day.isBefore(date));
  }

  public boolean isBeyondOverdraft() {
    return balance.compareTo(authorizedOverdraft.negate()) < 0;
  }

  private void assertNotFrozen() {
    if (isFrozen()) {
      throw new AccountFrozenException(iban);
    }
  }

  private void move(BigDecimal amount, String label, LocalDateTime at) {
    balance = balance.add(amount);
    entries.add(new Entry(at, amount, label));
  }

  public Iban iban() {
    return iban;
  }

  public AccountType type() {
    return type;
  }

  public List<CustomerId> holders() {
    return holders;
  }

  public BigDecimal authorizedOverdraft() {
    return authorizedOverdraft;
  }

  public LocalDate openedOn() {
    return openedOn;
  }

  public BigDecimal balance() {
    return balance;
  }

  public List<Entry> entries() {
    return List.copyOf(entries);
  }
}
