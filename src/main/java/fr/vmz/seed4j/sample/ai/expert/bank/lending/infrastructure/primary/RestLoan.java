package fr.vmz.seed4j.sample.ai.expert.bank.lending.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.Loan;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoanStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestLoan(
  UUID id,
  UUID borrower,
  String account,
  BigDecimal amount,
  int months,
  BigDecimal instalment,
  LocalDate signedOn,
  LoanStatus status,
  @Nullable LocalDate nextInstalmentOn,
  int paidInstalments,
  int unpaidInstalments
) {
  static RestLoan from(Loan loan) {
    return new RestLoan(
      loan.id().get(),
      loan.borrower().get(),
      loan.account().get(),
      loan.amount(),
      loan.months(),
      loan.instalment(),
      loan.signedOn(),
      loan.status(),
      loan.nextInstalmentOn().orElse(null),
      loan.paidInstalments(),
      loan.unpaidInstalments()
    );
  }
}
