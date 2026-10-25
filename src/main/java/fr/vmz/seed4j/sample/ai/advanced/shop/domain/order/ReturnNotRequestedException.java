package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.ShopRuleViolationException;

class ReturnNotRequestedException extends ShopRuleViolationException {

  ReturnNotRequestedException(CustomerOrderId order) {
    super("No return was requested for order " + order.get());
  }
}
