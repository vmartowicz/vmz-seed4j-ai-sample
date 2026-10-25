package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record LineToPlace(ProductId product, Quantity quantity) {
  public LineToPlace {
    Assert.notNull("product", product);
    Assert.notNull("quantity", quantity);
  }
}
