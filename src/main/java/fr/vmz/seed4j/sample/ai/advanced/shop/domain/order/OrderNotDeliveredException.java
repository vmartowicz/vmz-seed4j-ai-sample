package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.ShopRuleViolationException;

class OrderNotDeliveredException extends ShopRuleViolationException {

  OrderNotDeliveredException(CustomerOrderId order, OrderStatus status) {
    super("Order " + order.get() + " is " + status + ", only delivered orders can be returned");
  }
}
