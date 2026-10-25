package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Money;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class Order {

  private static final int RETURN_PERIOD_IN_DAYS = 14;

  private final OrderId id;
  private final CustomerEmail customer;
  private final List<OrderLine> lines;
  private OrderStatus status = OrderStatus.PENDING;
  private @Nullable LocalDate deliveredOn;

  Order(OrderId id, OrderToCreate order) {
    this.id = id;
    customer = order.customer();
    lines = order.lines();
  }

  public void pay() {
    moveTo(OrderStatus.PAID);
  }

  public void ship() {
    moveTo(OrderStatus.SHIPPED);
  }

  public void deliver(LocalDate today) {
    Assert.notNull("today", today);

    moveTo(OrderStatus.DELIVERED);
    deliveredOn = today;
  }

  public void cancel() {
    moveTo(OrderStatus.CANCELLED);
  }

  public void returnOrder(LocalDate today) {
    Assert.notNull("today", today);
    assertCanMoveTo(OrderStatus.RETURNED);

    var delivery = deliveredOn().orElseThrow();
    if (today.isAfter(delivery.plusDays(RETURN_PERIOD_IN_DAYS))) {
      throw new ReturnPeriodExpiredException(id, delivery);
    }

    status = OrderStatus.RETURNED;
  }

  private void moveTo(OrderStatus target) {
    assertCanMoveTo(target);

    status = target;
  }

  private void assertCanMoveTo(OrderStatus target) {
    if (!status.canMoveTo(target)) {
      throw new InvalidOrderTransitionException(id, status, target);
    }
  }

  public Money total() {
    return lines.stream().map(OrderLine::total).reduce(Money.ZERO, Money::plus);
  }

  public OrderId id() {
    return id;
  }

  public CustomerEmail customer() {
    return customer;
  }

  public List<OrderLine> lines() {
    return lines;
  }

  public OrderStatus status() {
    return status;
  }

  public Optional<LocalDate> deliveredOn() {
    return Optional.ofNullable(deliveredOn);
  }
}
