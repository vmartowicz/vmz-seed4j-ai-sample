package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;

public record Price(BigDecimal price) {
  static final int CENTS = 2;

  public Price {
    Assert.field("price", price).strictlyPositive();

    if (price.stripTrailingZeros().scale() > CENTS) {
      throw new InvalidPriceException(price);
    }

    price = price.setScale(CENTS);
  }

  public Money times(int quantity) {
    return new Money(price.multiply(BigDecimal.valueOf(quantity)));
  }

  public BigDecimal get() {
    return price();
  }
}
