package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class LoanOffice {

  private static final int MIN_CUSTOMER_MONTHS = 6;
  private static final int MAX_AGE_AT_END = 75;
  private static final int OVERDRAFT_LOOKBACK_MONTHS = 3;
  private static final BigDecimal MAX_DEBT_RATIO = new BigDecimal("0.35");

  private final LoansRepository loans;
  private final Borrowers borrowers;
  private final LoanAccounts accounts;

  public LoanOffice(LoansRepository loans, Borrowers borrowers, LoanAccounts accounts) {
    this.loans = loans;
    this.borrowers = borrowers;
    this.accounts = accounts;
  }

  public Loan apply(LoanRequest request, LocalDate today) {
    Assert.notNull("request", request);

    var borrower = borrowers.borrower(request.borrower()).orElseThrow(() -> new UnknownBorrowerException(request.borrower()));
    if (borrower.customerSince().isAfter(today.minusMonths(MIN_CUSTOMER_MONTHS))) {
      throw new LoanRefusedException("customers need 6 months of history with the bank");
    }

    if (!today.plusMonths(request.months()).isBefore(borrower.birthDate().plusYears(MAX_AGE_AT_END))) {
      throw new LoanRefusedException("the borrower must be under 75 at the end of the loan");
    }

    if (accounts.wasInUnauthorizedOverdraftSince(borrower.id(), today.minusMonths(OVERDRAFT_LOOKBACK_MONTHS))) {
      throw new LoanRefusedException("unauthorized overdraft in the last 3 months");
    }

    var account = accounts.currentAccountOf(borrower.id()).orElseThrow(() -> new LoanRefusedException("no current account to pay the instalments"));
    var loan = Loan.sign(request, account, today);
    var instalments = loans
      .all()
      .stream()
      .filter(running -> running.borrower().equals(borrower.id()) && running.isRunning())
      .map(Loan::instalment)
      .reduce(loan.instalment(), BigDecimal::add);
    if (instalments.divide(borrower.monthlyIncome(), 4, RoundingMode.HALF_UP).compareTo(MAX_DEBT_RATIO) > 0) {
      throw new LoanRefusedException("the debt ratio would exceed 35%");
    }

    loans.save(loan);
    return loan;
  }

  public void disburse(LocalDate today) {
    loans
      .all()
      .stream()
      .filter(loan -> loan.disburseIfDue(today))
      .forEach(loan -> {
        accounts.credit(loan.account(), loan.amount(), "Personal loan " + loan.id().get());
        loans.save(loan);
      });
  }

  public void collectInstalments(LocalDate today) {
    loans
      .all()
      .stream()
      .filter(loan -> loan.isInstalmentDueOn(today))
      .forEach(loan -> {
        if (accounts.collect(loan.account(), loan.instalment(), "Loan instalment " + loan.id().get())) {
          loan.instalmentPaid();
        } else {
          loan.instalmentUnpaid();
        }
        loans.save(loan);
      });
  }

  public Loan loan(LoanId id) {
    return loans.loan(id);
  }
}
