package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import java.util.List;
import java.util.Optional;

public interface CustomerOrdersRepository {
  void save(CustomerOrder order);

  Optional<CustomerOrder> get(CustomerOrderId order);

  List<CustomerOrder> ofCustomer(CustomerEmail customer);

  default CustomerOrder order(CustomerOrderId order) {
    return get(order).orElseThrow(() -> new UnknownCustomerOrderException(order));
  }
}
