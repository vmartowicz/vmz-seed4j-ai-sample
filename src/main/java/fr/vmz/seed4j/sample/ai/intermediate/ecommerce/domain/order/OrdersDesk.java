package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductsRepository;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class OrdersDesk {

  private final ProductsRepository products;
  private final OrdersRepository orders;
  private final OrderNotifier notifier;

  public OrdersDesk(ProductsRepository products, OrdersRepository orders, OrderNotifier notifier) {
    this.products = products;
    this.orders = orders;
    this.notifier = notifier;
  }

  public Order place(CustomerEmail customer, List<LineToPlace> linesToPlace) {
    Assert.field("lines", linesToPlace).noNullElement();

    var lines = linesToPlace
      .stream()
      .map(line -> new OrderLine(line.product(), line.quantity(), products.product(line.product()).price()))
      .toList();
    var toCreate = new OrderToCreate(customer, lines);

    lines.forEach(line -> products.product(line.product()).assertAvailable(line.quantity().get()));
    lines.forEach(line -> {
      var product = products.product(line.product());
      product.reserve(line.quantity().get());
      products.save(product);
    });

    var order = toCreate.create();
    orders.save(order);
    notifyStatus(order);

    return order;
  }

  public Order pay(OrderId order) {
    return change(order, Order::pay);
  }

  public Order ship(OrderId order) {
    return change(order, Order::ship);
  }

  public Order deliver(OrderId order, LocalDate today) {
    return change(order, toDeliver -> toDeliver.deliver(today));
  }

  public Order cancel(OrderId order) {
    return change(order, toCancel -> {
      toCancel.cancel();
      releaseStock(toCancel);
    });
  }

  public Order returnOrder(OrderId order, LocalDate today) {
    return change(order, toReturn -> {
      toReturn.returnOrder(today);
      releaseStock(toReturn);
    });
  }

  private Order change(OrderId orderId, Consumer<Order> transition) {
    var order = orders.order(orderId);
    transition.accept(order);
    orders.save(order);
    notifyStatus(order);

    return order;
  }

  private void releaseStock(Order order) {
    order
      .lines()
      .forEach(line -> {
        var product = products.product(line.product());
        product.release(line.quantity().get());
        products.save(product);
      });
  }

  private void notifyStatus(Order order) {
    notifier.statusChanged(new OrderStatusChanged(order.id(), order.customer(), order.status()));
  }
}
