package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestOrder(
  UUID id,
  String customerEmail,
  String status,
  List<RestOrderLine> lines,
  BigDecimal total,
  @Nullable LocalDate deliveredOn
) {
  static RestOrder from(Order order) {
    return new RestOrder(
      order.id().get(),
      order.customer().get(),
      order.status().name(),
      order.lines().stream().map(RestOrderLine::from).toList(),
      order.total().get(),
      order.deliveredOn().orElse(null)
    );
  }
}
