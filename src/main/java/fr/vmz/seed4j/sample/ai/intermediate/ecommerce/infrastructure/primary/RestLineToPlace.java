package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.LineToPlace;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.Quantity;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductId;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestLineToPlace(@Nullable UUID productId, int quantity) {
  LineToPlace toDomain() {
    return new LineToPlace(new ProductId(productId), new Quantity(quantity));
  }
}
