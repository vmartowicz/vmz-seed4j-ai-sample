package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class CustomerOrder {

  private static final int RETURN_PERIOD_IN_DAYS = 14;
  private static final int REFUND_DELAY_IN_DAYS = 5;

  private final CustomerOrderId id;
  private final CustomerEmail customer;
  private final List<OrderLine> lines;
  private final OrderPricing pricing;
  private final List<StatusChange> history = new ArrayList<>();
  private OrderStatus status = OrderStatus.AWAITING_PAYMENT;
  private @Nullable TrackingNumber trackingNumber;
  private @Nullable LocalDate deliveredOn;
  private boolean returnRequested;
  private @Nullable Refund refund;

  CustomerOrder(CustomerOrderId id, OrderToPlace order, OrderPricing pricing, LocalDateTime now) {
    Assert.notNull("now", now);

    this.id = id;
    customer = order.customer();
    lines = order.lines();
    this.pricing = pricing;
    history.add(new StatusChange(status, now));
  }

  public void pay(LocalDateTime now) {
    moveTo(OrderStatus.PAID, now);
  }

  public void prepare(LocalDateTime now) {
    moveTo(OrderStatus.IN_PREPARATION, now);
  }

  public void ship(TrackingNumber tracking, LocalDateTime now) {
    Assert.notNull("tracking", tracking);

    moveTo(OrderStatus.SHIPPED, now);
    trackingNumber = tracking;
  }

  public void deliver(LocalDateTime now) {
    moveTo(OrderStatus.DELIVERED, now);
    deliveredOn = now.toLocalDate();
  }

  public void cancel(LocalDateTime now) {
    var paid = status != OrderStatus.AWAITING_PAYMENT;

    moveTo(OrderStatus.CANCELLED, now);
    if (paid) {
      refund = new Refund(pricing.total(), refundDate(now));
    }
  }

  public void requestReturn(LocalDateTime now) {
    Assert.notNull("now", now);

    if (status != OrderStatus.DELIVERED) {
      throw new OrderNotDeliveredException(id, status);
    }

    if (returnRequested) {
      throw new ReturnAlreadyRequestedException(id);
    }

    var delivery = deliveredOn().orElseThrow();
    if (now.toLocalDate().isAfter(delivery.plusDays(RETURN_PERIOD_IN_DAYS))) {
      throw new ReturnPeriodExpiredException(id, delivery);
    }

    returnRequested = true;
  }

  public void receiveReturn(LocalDateTime now) {
    if (!returnRequested) {
      throw new ReturnNotRequestedException(id);
    }

    moveTo(OrderStatus.RETURNED, now);
    refund = new Refund(pricing.itemsAfterDiscount(), refundDate(now));
  }

  private void moveTo(OrderStatus target, LocalDateTime now) {
    Assert.notNull("now", now);

    if (!status.canMoveTo(target)) {
      throw new InvalidOrderTransitionException(id, status, target);
    }

    status = target;
    history.add(new StatusChange(target, now));
  }

  private static LocalDate refundDate(LocalDateTime now) {
    return now.toLocalDate().plusDays(REFUND_DELAY_IN_DAYS);
  }

  public CustomerOrderId id() {
    return id;
  }

  public CustomerEmail customer() {
    return customer;
  }

  public List<OrderLine> lines() {
    return lines;
  }

  public OrderPricing pricing() {
    return pricing;
  }

  public OrderStatus status() {
    return status;
  }

  public List<StatusChange> history() {
    return List.copyOf(history);
  }

  public Optional<TrackingNumber> trackingNumber() {
    return Optional.ofNullable(trackingNumber);
  }

  public Optional<LocalDate> deliveredOn() {
    return Optional.ofNullable(deliveredOn);
  }

  public boolean returnRequested() {
    return returnRequested;
  }

  public Optional<Refund> refund() {
    return Optional.ofNullable(refund);
  }
}
