package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.EcommerceRuleViolationException;
import java.time.LocalDate;

class ReturnPeriodExpiredException extends EcommerceRuleViolationException {

  ReturnPeriodExpiredException(OrderId order, LocalDate deliveredOn) {
    super("Order " + order.get() + " was delivered on " + deliveredOn + ": the 14 days return period has expired");
  }
}
