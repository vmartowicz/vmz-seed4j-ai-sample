package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.OrderLine;
import java.math.BigDecimal;
import java.util.UUID;

record RestOrderLine(UUID articleId, int quantity, BigDecimal unitPrice, BigDecimal total) {
  static RestOrderLine from(OrderLine line) {
    return new RestOrderLine(line.article().get(), line.quantity().get(), line.unitPrice().get(), line.total().get());
  }
}
