package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Isbn;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import java.time.LocalDate;

public final class LoansFixture {

  public static final LocalDate TODAY = LocalDate.of(2026, 3, 10);

  private LoansFixture() {}

  public static Loan loan(MemberId member, Isbn book, LocalDate borrowedOn) {
    return new LoanToCreate(member, book, borrowedOn).create();
  }

  public static Loan returnedLoan(MemberId member, Isbn book, LocalDate borrowedOn, int lateDays) {
    var loan = loan(member, book, borrowedOn);
    loan.giveBack(loan.dueOn().plusDays(lateDays));

    return loan;
  }
}
