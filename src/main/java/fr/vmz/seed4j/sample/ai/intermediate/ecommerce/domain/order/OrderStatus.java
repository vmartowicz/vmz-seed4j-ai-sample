package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
  PENDING,
  PAID,
  SHIPPED,
  DELIVERED,
  CANCELLED,
  RETURNED;

  public boolean canMoveTo(OrderStatus target) {
    return nextStatuses().contains(target);
  }

  private Set<OrderStatus> nextStatuses() {
    return switch (this) {
      case PENDING -> EnumSet.of(PAID, CANCELLED);
      case PAID -> EnumSet.of(SHIPPED, CANCELLED);
      case SHIPPED -> EnumSet.of(DELIVERED);
      case DELIVERED -> EnumSet.of(RETURNED);
      case CANCELLED, RETURNED -> EnumSet.noneOf(OrderStatus.class);
    };
  }
}
