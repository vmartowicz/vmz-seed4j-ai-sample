package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.EcommerceRuleViolationException;

class InvalidOrderTransitionException extends EcommerceRuleViolationException {

  InvalidOrderTransitionException(OrderId order, OrderStatus from, OrderStatus to) {
    super("Order " + order.get() + " can't go from " + from + " to " + to);
  }
}
