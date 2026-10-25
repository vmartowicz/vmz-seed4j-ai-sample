package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Price;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class CustomerOrdersFixture {

  public static final LocalDateTime NOW = LocalDateTime.of(2026, 3, 10, 10, 0);

  private CustomerOrdersFixture() {}

  public static CustomerEmail jane() {
    return new CustomerEmail("jane@shop.com");
  }

  public static OrderLine line(ArticleId article, int quantity, String unitPrice) {
    return new OrderLine(article, new Quantity(quantity), new Price(new BigDecimal(unitPrice)));
  }

  public static CustomerOrder orderOf(String unitPrice, int quantity) {
    return new OrderToPlace(jane(), List.of(line(ArticleId.newId(), quantity, unitPrice))).place(false, NOW);
  }

  public static CustomerOrder order() {
    return orderOf("30.00", 2);
  }

  public static CustomerOrder deliveredOrder() {
    var order = order();
    order.pay(NOW);
    order.prepare(NOW);
    order.ship(new TrackingNumber("6A12345678901"), NOW);
    order.deliver(NOW);

    return order;
  }
}
