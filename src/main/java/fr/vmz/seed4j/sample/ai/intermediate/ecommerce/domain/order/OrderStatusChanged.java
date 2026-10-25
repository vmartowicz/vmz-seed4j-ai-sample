package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record OrderStatusChanged(OrderId order, CustomerEmail customer, OrderStatus status) {
  public OrderStatusChanged {
    Assert.notNull("order", order);
    Assert.notNull("customer", customer);
    Assert.notNull("status", status);
  }
}
