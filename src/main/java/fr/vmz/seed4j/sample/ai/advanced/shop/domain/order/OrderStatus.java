package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
  AWAITING_PAYMENT,
  PAID,
  IN_PREPARATION,
  SHIPPED,
  DELIVERED,
  RETURNED,
  CANCELLED;

  public boolean canMoveTo(OrderStatus target) {
    return nextStatuses().contains(target);
  }

  private Set<OrderStatus> nextStatuses() {
    return switch (this) {
      case AWAITING_PAYMENT -> EnumSet.of(PAID, CANCELLED);
      case PAID -> EnumSet.of(IN_PREPARATION, CANCELLED);
      case IN_PREPARATION -> EnumSet.of(SHIPPED, CANCELLED);
      case SHIPPED -> EnumSet.of(DELIVERED);
      case DELIVERED -> EnumSet.of(RETURNED);
      case RETURNED, CANCELLED -> EnumSet.noneOf(OrderStatus.class);
    };
  }
}
