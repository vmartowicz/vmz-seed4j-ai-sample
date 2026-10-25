package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.secondary.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerEmail;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrder;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrderId;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrdersRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryCustomerOrdersRepository implements CustomerOrdersRepository {

  private final Map<CustomerOrderId, CustomerOrder> orders = new ConcurrentHashMap<>();

  @Override
  public void save(CustomerOrder order) {
    orders.put(order.id(), order);
  }

  @Override
  public Optional<CustomerOrder> get(CustomerOrderId order) {
    return Optional.ofNullable(orders.get(order));
  }

  @Override
  public List<CustomerOrder> ofCustomer(CustomerEmail customer) {
    return orders
      .values()
      .stream()
      .filter(order -> order.customer().equals(customer))
      .toList();
  }
}
