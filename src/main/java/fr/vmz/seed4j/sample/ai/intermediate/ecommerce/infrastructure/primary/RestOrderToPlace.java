package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.CustomerEmail;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.LineToPlace;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

record RestOrderToPlace(@Nullable String customerEmail, @Nullable List<RestLineToPlace> lines) {
  CustomerEmail customer() {
    return new CustomerEmail(customerEmail);
  }

  List<LineToPlace> linesToPlace() {
    return Objects.requireNonNullElse(lines, List.<RestLineToPlace>of()).stream().map(RestLineToPlace::toDomain).toList();
  }
}
