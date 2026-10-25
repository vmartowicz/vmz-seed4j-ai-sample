package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Money;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Price;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record OrderLine(ArticleId article, Quantity quantity, Price unitPrice) {
  public OrderLine {
    Assert.notNull("article", article);
    Assert.notNull("quantity", quantity);
    Assert.notNull("unitPrice", unitPrice);
  }

  public Money total() {
    return unitPrice.times(quantity.get());
  }
}
