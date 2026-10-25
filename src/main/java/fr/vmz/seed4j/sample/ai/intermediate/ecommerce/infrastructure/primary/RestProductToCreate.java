package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Price;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductName;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductToCreate;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.Stock;
import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

record RestProductToCreate(@Nullable String name, @Nullable BigDecimal unitPrice, int stock) {
  ProductToCreate toDomain() {
    return new ProductToCreate(new ProductName(name), new Price(unitPrice), new Stock(stock));
  }
}
