package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Isbn;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public record LoanToCreate(MemberId member, Isbn book, LocalDate borrowedOn) {
  public LoanToCreate {
    Assert.notNull("member", member);
    Assert.notNull("book", book);
    Assert.notNull("borrowedOn", borrowedOn);
  }

  public Loan create() {
    return new Loan(LoanId.newId(), this);
  }
}
