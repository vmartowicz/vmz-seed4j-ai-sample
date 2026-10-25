package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Money;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Price;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record OrderLine(ProductId product, Quantity quantity, Price unitPrice) {
  public OrderLine {
    Assert.notNull("product", product);
    Assert.notNull("quantity", quantity);
    Assert.notNull("unitPrice", unitPrice);
  }

  public Money total() {
    return unitPrice.times(quantity.get());
  }
}
