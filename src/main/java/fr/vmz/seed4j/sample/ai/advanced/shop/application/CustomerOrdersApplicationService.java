package fr.vmz.seed4j.sample.ai.advanced.shop.application;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticlesRepository;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerEmail;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrder;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrderId;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrdersDesk;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrdersRepository;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.LineToOrder;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.TrackingNumber;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CustomerOrdersApplicationService {

  private final CustomerOrdersRepository orders;
  private final CustomerOrdersDesk desk;
  private final Clock clock;

  public CustomerOrdersApplicationService(ArticlesRepository articles, CustomerOrdersRepository orders, Clock clock) {
    this.orders = orders;
    desk = new CustomerOrdersDesk(articles, orders);
    this.clock = clock;
  }

  public CustomerOrder place(CustomerEmail customer, List<LineToOrder> lines) {
    return desk.place(customer, lines, now());
  }

  public CustomerOrder get(CustomerOrderId order) {
    return orders.order(order);
  }

  public CustomerOrder pay(CustomerOrderId order) {
    return desk.pay(order, now());
  }

  public CustomerOrder prepare(CustomerOrderId order) {
    return desk.prepare(order, now());
  }

  public CustomerOrder ship(CustomerOrderId order, TrackingNumber tracking) {
    return desk.ship(order, tracking, now());
  }

  public CustomerOrder deliver(CustomerOrderId order) {
    return desk.deliver(order, now());
  }

  public CustomerOrder cancel(CustomerOrderId order) {
    return desk.cancel(order, now());
  }

  public CustomerOrder requestReturn(CustomerOrderId order) {
    return desk.requestReturn(order, now());
  }

  public CustomerOrder receiveReturn(CustomerOrderId order) {
    return desk.receiveReturn(order, now());
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
