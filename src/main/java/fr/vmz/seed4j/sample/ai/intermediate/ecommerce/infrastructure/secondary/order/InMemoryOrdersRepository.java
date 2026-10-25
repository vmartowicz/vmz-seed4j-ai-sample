package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.secondary.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.Order;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderId;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrdersRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryOrdersRepository implements OrdersRepository {

  private final Map<OrderId, Order> orders = new ConcurrentHashMap<>();

  @Override
  public void save(Order order) {
    orders.put(order.id(), order);
  }

  @Override
  public Optional<Order> get(OrderId order) {
    return Optional.ofNullable(orders.get(order));
  }
}
