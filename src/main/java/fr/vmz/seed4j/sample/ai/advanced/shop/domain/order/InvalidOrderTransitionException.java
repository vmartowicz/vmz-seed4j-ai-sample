package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.ShopRuleViolationException;

class InvalidOrderTransitionException extends ShopRuleViolationException {

  InvalidOrderTransitionException(CustomerOrderId order, OrderStatus from, OrderStatus to) {
    super("Order " + order.get() + " can't go from " + from + " to " + to);
  }
}
