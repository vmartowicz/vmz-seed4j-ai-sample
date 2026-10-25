package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.Loan;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

record RestFine(UUID loanId, String isbn, LocalDate dueOn, LocalDate returnedOn, long lateDays, BigDecimal amount) {
  static RestFine from(Loan loan) {
    return new RestFine(
      loan.id().get(),
      loan.book().get(),
      loan.dueOn(),
      loan.returnedOn().orElseThrow(),
      loan.lateDays(),
      loan.fine().get()
    );
  }
}
