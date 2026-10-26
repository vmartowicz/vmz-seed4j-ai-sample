package fr.vmz.seed4j.sample.ai.expert.bank.lending.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoanRequest;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.math.BigDecimal;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestLoanRequest(@Nullable UUID borrower, @Nullable BigDecimal amount, int months) {
  LoanRequest toDomain() {
    return new LoanRequest(new CustomerId(borrower), amount, months);
  }
}
