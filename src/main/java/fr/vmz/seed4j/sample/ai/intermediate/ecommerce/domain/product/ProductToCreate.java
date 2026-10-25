package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Price;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record ProductToCreate(ProductName name, Price price, Stock stock) {
  public ProductToCreate {
    Assert.notNull("name", name);
    Assert.notNull("price", price);
    Assert.notNull("stock", stock);
  }

  public Product create() {
    return new Product(ProductId.newId(), this);
  }
}
