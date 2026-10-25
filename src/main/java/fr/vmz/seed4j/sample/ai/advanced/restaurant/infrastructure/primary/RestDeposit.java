package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.Deposit;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.DepositStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

record RestDeposit(BigDecimal amount, LocalDateTime dueBy, DepositStatus status) {
  static RestDeposit from(Deposit deposit) {
    return new RestDeposit(deposit.amount(), deposit.dueBy(), deposit.status());
  }
}
