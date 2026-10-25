package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;

public record StatusChange(OrderStatus status, LocalDateTime at) {
  public StatusChange {
    Assert.notNull("status", status);
    Assert.notNull("at", at);
  }
}
