package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.Deposit;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.DepositStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestDeposit(
  UUID contractId,
  String customer,
  BigDecimal amount,
  DepositStatus status,
  @Nullable LocalDate returnedOn,
  BigDecimal retained,
  BigDecimal released
) {
  static RestDeposit from(Deposit deposit) {
    return new RestDeposit(
      deposit.contract(),
      deposit.customer().get(),
      deposit.amount(),
      deposit.status(),
      deposit.returnedOn().orElse(null),
      deposit.retained(),
      deposit.released()
    );
  }
}
