package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Money;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public record Refund(Money amount, LocalDate dueBy) {
  public Refund {
    Assert.notNull("amount", amount);
    Assert.notNull("dueBy", dueBy);
  }
}
