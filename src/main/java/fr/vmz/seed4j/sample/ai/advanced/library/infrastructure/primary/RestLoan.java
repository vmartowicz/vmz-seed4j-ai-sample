package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.Loan;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestLoan(
  UUID id,
  UUID memberId,
  String isbn,
  LocalDate borrowedOn,
  LocalDate dueOn,
  @Nullable LocalDate returnedOn,
  long lateDays,
  BigDecimal fine
) {
  static RestLoan from(Loan loan) {
    return new RestLoan(
      loan.id().get(),
      loan.member().get(),
      loan.book().get(),
      loan.borrowedOn(),
      loan.dueOn(),
      loan.returnedOn().orElse(null),
      loan.lateDays(),
      loan.fine().get()
    );
  }
}
