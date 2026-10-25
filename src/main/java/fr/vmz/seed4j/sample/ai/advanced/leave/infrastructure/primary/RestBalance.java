package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.BalanceSummary;
import java.math.BigDecimal;

record RestBalance(int year, BigDecimal entitlement, BigDecimal approved, BigDecimal pending, BigDecimal remaining) {
  static RestBalance from(BalanceSummary summary) {
    return new RestBalance(
      summary.year().getValue(),
      summary.entitlement().get(),
      summary.approved().get(),
      summary.pending().get(),
      summary.remaining().get()
    );
  }
}
