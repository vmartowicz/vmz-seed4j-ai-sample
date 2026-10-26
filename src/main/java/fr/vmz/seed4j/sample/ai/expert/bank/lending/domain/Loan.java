package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class Loan {

  private static final int COOLING_OFF_DAYS = 14;
  private static final int UNPAID_BEFORE_COLLECTION = 2;

  private final LoanId id;
  private final CustomerId borrower;
  private final Iban account;
  private final BigDecimal amount;
  private final int months;
  private final BigDecimal instalment;
  private final LocalDate signedOn;
  private LoanStatus status;
  private @Nullable LocalDate nextInstalmentOn;
  private int paidInstalments;
  private int unpaidInstalments;

  private Loan(LoanRequest request, Iban account, LocalDate signedOn) {
    id = LoanId.newId();
    borrower = request.borrower();
    this.account = account;
    amount = request.amount();
    months = request.months();
    instalment = LoanTerms.monthlyInstalment(amount, months);
    this.signedOn = signedOn;
    status = LoanStatus.SIGNED;
  }

  public static Loan sign(LoanRequest request, Iban account, LocalDate today) {
    return new Loan(request, account, today);
  }

  public boolean disburseIfDue(LocalDate today) {
    if (status != LoanStatus.SIGNED || today.isBefore(signedOn.plusDays(COOLING_OFF_DAYS))) {
      return false;
    }

    status = LoanStatus.ACTIVE;
    nextInstalmentOn = today.plusMonths(1);
    return true;
  }

  public boolean isInstalmentDueOn(LocalDate date) {
    return status == LoanStatus.ACTIVE && nextInstalmentOn().filter(due -> !due.isAfter(date)).isPresent();
  }

  public void instalmentPaid() {
    paidInstalments++;
    moveToNextInstalment();
    if (paidInstalments == months) {
      status = LoanStatus.REPAID;
    }
  }

  public void instalmentUnpaid() {
    unpaidInstalments++;
    moveToNextInstalment();
    if (unpaidInstalments >= UNPAID_BEFORE_COLLECTION) {
      status = LoanStatus.IN_COLLECTION;
    }
  }

  public boolean isRunning() {
    return status != LoanStatus.REPAID;
  }

  private void moveToNextInstalment() {
    nextInstalmentOn = nextInstalmentOn().map(due -> due.plusMonths(1)).orElse(null);
  }

  public LoanId id() {
    return id;
  }

  public CustomerId borrower() {
    return borrower;
  }

  public Iban account() {
    return account;
  }

  public BigDecimal amount() {
    return amount;
  }

  public int months() {
    return months;
  }

  public BigDecimal instalment() {
    return instalment;
  }

  public LocalDate signedOn() {
    return signedOn;
  }

  public LoanStatus status() {
    return status;
  }

  public Optional<LocalDate> nextInstalmentOn() {
    return Optional.ofNullable(nextInstalmentOn);
  }

  public int paidInstalments() {
    return paidInstalments;
  }

  public int unpaidInstalments() {
    return unpaidInstalments;
  }
}
