package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.ShopRuleViolationException;
import java.time.LocalDate;

class ReturnPeriodExpiredException extends ShopRuleViolationException {

  ReturnPeriodExpiredException(CustomerOrderId order, LocalDate deliveredOn) {
    super("Order " + order.get() + " was delivered on " + deliveredOn + ": the 14 days return period has expired");
  }
}
