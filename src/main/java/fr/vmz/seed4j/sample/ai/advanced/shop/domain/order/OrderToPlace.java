package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public record OrderToPlace(CustomerEmail customer, List<OrderLine> lines) {
  public OrderToPlace {
    Assert.notNull("customer", customer);
    Assert.field("lines", lines).notEmpty().noNullElement();

    var articles = new HashSet<ArticleId>();
    lines
      .stream()
      .map(OrderLine::article)
      .filter(article -> !articles.add(article))
      .findFirst()
      .ifPresent(article -> {
        throw new DuplicatedOrderLineException(article);
      });

    lines = List.copyOf(lines);
  }

  public CustomerOrder place(boolean loyalCustomer, LocalDateTime now) {
    return new CustomerOrder(CustomerOrderId.newId(), this, OrderPricing.of(lines, loyalCustomer), now);
  }
}
