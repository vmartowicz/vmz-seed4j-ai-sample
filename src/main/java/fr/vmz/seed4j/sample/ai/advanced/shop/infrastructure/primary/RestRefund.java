package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.Refund;
import java.math.BigDecimal;
import java.time.LocalDate;

record RestRefund(BigDecimal amount, LocalDate dueBy) {
  static RestRefund from(Refund refund) {
    return new RestRefund(refund.amount().get(), refund.dueBy());
  }
}
