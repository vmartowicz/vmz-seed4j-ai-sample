package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.Product;
import java.math.BigDecimal;
import java.util.UUID;

record RestProduct(UUID id, String name, BigDecimal unitPrice, int stock) {
  static RestProduct from(Product product) {
    return new RestProduct(product.id().get(), product.name().get(), product.price().get(), product.stock().get());
  }
}
