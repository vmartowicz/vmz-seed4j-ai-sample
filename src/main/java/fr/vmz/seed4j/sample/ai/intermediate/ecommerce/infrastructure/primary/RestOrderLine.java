package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderLine;
import java.math.BigDecimal;
import java.util.UUID;

record RestOrderLine(UUID productId, int quantity, BigDecimal unitPrice, BigDecimal total) {
  static RestOrderLine from(OrderLine line) {
    return new RestOrderLine(line.product().get(), line.quantity().get(), line.unitPrice().get(), line.total().get());
  }
}
