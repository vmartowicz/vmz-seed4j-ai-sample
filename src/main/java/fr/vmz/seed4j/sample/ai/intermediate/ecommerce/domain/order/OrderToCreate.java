package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.HashSet;
import java.util.List;

public record OrderToCreate(CustomerEmail customer, List<OrderLine> lines) {
  public OrderToCreate {
    Assert.notNull("customer", customer);
    Assert.field("lines", lines).notEmpty().noNullElement();

    var products = new HashSet<ProductId>();
    lines
      .stream()
      .map(OrderLine::product)
      .filter(product -> !products.add(product))
      .findFirst()
      .ifPresent(product -> {
        throw new DuplicatedOrderLineException(product);
      });

    lines = List.copyOf(lines);
  }

  public Order create() {
    return new Order(OrderId.newId(), this);
  }
}
