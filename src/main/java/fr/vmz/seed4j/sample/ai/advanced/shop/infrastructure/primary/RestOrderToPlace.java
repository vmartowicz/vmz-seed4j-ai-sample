package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerEmail;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.LineToOrder;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

record RestOrderToPlace(@Nullable String customerEmail, @Nullable List<RestLineToOrder> lines) {
  CustomerEmail customer() {
    return new CustomerEmail(customerEmail);
  }

  List<LineToOrder> linesToOrder() {
    return Objects.requireNonNullElse(lines, List.<RestLineToOrder>of()).stream().map(RestLineToOrder::toDomain).toList();
  }
}
