package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticlesRepository;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class CustomerOrdersDesk {

  private static final long ORDERS_FOR_LOYALTY = 5;

  private final ArticlesRepository articles;
  private final CustomerOrdersRepository orders;

  public CustomerOrdersDesk(ArticlesRepository articles, CustomerOrdersRepository orders) {
    this.articles = articles;
    this.orders = orders;
  }

  public CustomerOrder place(CustomerEmail customer, List<LineToOrder> linesToOrder, LocalDateTime now) {
    Assert.notNull("customer", customer);
    Assert.field("lines", linesToOrder).noNullElement();

    var lines = linesToOrder
      .stream()
      .map(line -> new OrderLine(line.article(), line.quantity(), articles.article(line.article()).price()))
      .toList();
    var toPlace = new OrderToPlace(customer, lines);

    lines.forEach(line -> articles.article(line.article()).assertAvailable(line.quantity().get()));
    lines.forEach(line -> {
      var article = articles.article(line.article());
      article.reserve(line.quantity().get());
      articles.save(article);
    });

    var order = toPlace.place(isLoyal(customer), now);
    orders.save(order);

    return order;
  }

  private boolean isLoyal(CustomerEmail customer) {
    var deliveredOrders = orders
      .ofCustomer(customer)
      .stream()
      .filter(order -> order.status() == OrderStatus.DELIVERED)
      .count();

    return deliveredOrders >= ORDERS_FOR_LOYALTY;
  }

  public CustomerOrder pay(CustomerOrderId order, LocalDateTime now) {
    return change(order, toPay -> toPay.pay(now));
  }

  public CustomerOrder prepare(CustomerOrderId order, LocalDateTime now) {
    return change(order, toPrepare -> toPrepare.prepare(now));
  }

  public CustomerOrder ship(CustomerOrderId order, TrackingNumber tracking, LocalDateTime now) {
    return change(order, toShip -> toShip.ship(tracking, now));
  }

  public CustomerOrder deliver(CustomerOrderId order, LocalDateTime now) {
    return change(order, toDeliver -> toDeliver.deliver(now));
  }

  public CustomerOrder cancel(CustomerOrderId order, LocalDateTime now) {
    return change(order, toCancel -> {
      toCancel.cancel(now);
      releaseStock(toCancel);
    });
  }

  public CustomerOrder requestReturn(CustomerOrderId order, LocalDateTime now) {
    return change(order, toReturn -> toReturn.requestReturn(now));
  }

  public CustomerOrder receiveReturn(CustomerOrderId order, LocalDateTime now) {
    return change(order, returned -> {
      returned.receiveReturn(now);
      releaseStock(returned);
    });
  }

  private CustomerOrder change(CustomerOrderId orderId, Consumer<CustomerOrder> change) {
    var order = orders.order(orderId);
    change.accept(order);
    orders.save(order);

    return order;
  }

  private void releaseStock(CustomerOrder order) {
    order
      .lines()
      .forEach(line -> {
        var article = articles.article(line.article());
        article.release(line.quantity().get());
        articles.save(article);
      });
  }
}
