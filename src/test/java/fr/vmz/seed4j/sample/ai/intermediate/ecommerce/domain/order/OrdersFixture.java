package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Price;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class OrdersFixture {

  public static final LocalDate TODAY = LocalDate.of(2026, 3, 10);

  private OrdersFixture() {}

  public static CustomerEmail jane() {
    return new CustomerEmail("jane@shop.com");
  }

  public static OrderLine line(ProductId product, int quantity, String unitPrice) {
    return new OrderLine(product, new Quantity(quantity), new Price(new BigDecimal(unitPrice)));
  }

  public static Order order() {
    return new OrderToCreate(jane(), List.of(line(ProductId.newId(), 2, "19.99"), line(ProductId.newId(), 1, "8.50"))).create();
  }

  public static Order orderIn(OrderStatus status) {
    var order = order();
    switch (status) {
      case PENDING -> {}
      case PAID -> order.pay();
      case SHIPPED -> {
        order.pay();
        order.ship();
      }
      default -> {
        order.pay();
        order.ship();
        order.deliver(TODAY);
      }
    }

    return order;
  }
}
