package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class LendingFixture {

  public static final LocalDate TODAY = LocalDate.of(2026, 6, 1);
  public static final Iban CURRENT_ACCOUNT = Iban.generate();

  private LendingFixture() {}

  public static Borrower borrower(String income) {
    return new Borrower(CustomerId.newId(), LocalDate.of(1980, 1, 1), new BigDecimal(income), TODAY.minusYears(2));
  }

  public static LoanRequest request(Borrower borrower, String amount, int months) {
    return new LoanRequest(borrower.id(), new BigDecimal(amount), months);
  }

  public static Loan signed() {
    return Loan.sign(request(borrower("3000"), "10000", 24), CURRENT_ACCOUNT, TODAY);
  }

  public static Loan active() {
    var loan = signed();
    loan.disburseIfDue(TODAY.plusDays(14));

    return loan;
  }
}
