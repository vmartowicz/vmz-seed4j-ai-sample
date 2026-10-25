package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import java.util.Optional;

public interface OrdersRepository {
  void save(Order order);

  Optional<Order> get(OrderId order);

  default Order order(OrderId order) {
    return get(order).orElseThrow(() -> new UnknownOrderException(order));
  }
}
