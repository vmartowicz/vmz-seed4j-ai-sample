package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.application;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.CustomerEmail;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.LineToPlace;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.Order;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderId;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderNotifier;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrdersDesk;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrdersRepository;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductsRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrdersApplicationService {

  private final OrdersRepository orders;
  private final OrdersDesk desk;
  private final Clock clock;

  public OrdersApplicationService(ProductsRepository products, OrdersRepository orders, OrderNotifier notifier, Clock clock) {
    this.orders = orders;
    desk = new OrdersDesk(products, orders, notifier);
    this.clock = clock;
  }

  public Order place(CustomerEmail customer, List<LineToPlace> lines) {
    return desk.place(customer, lines);
  }

  public Order get(OrderId order) {
    return orders.order(order);
  }

  public Order pay(OrderId order) {
    return desk.pay(order);
  }

  public Order ship(OrderId order) {
    return desk.ship(order);
  }

  public Order deliver(OrderId order) {
    return desk.deliver(order, today());
  }

  public Order cancel(OrderId order) {
    return desk.cancel(order);
  }

  public Order returnOrder(OrderId order) {
    return desk.returnOrder(order, today());
  }

  private LocalDate today() {
    return LocalDate.now(clock);
  }
}
