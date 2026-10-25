package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.ShopRuleViolationException;

class ReturnAlreadyRequestedException extends ShopRuleViolationException {

  ReturnAlreadyRequestedException(CustomerOrderId order) {
    super("A return is already requested for order " + order.get());
  }
}
