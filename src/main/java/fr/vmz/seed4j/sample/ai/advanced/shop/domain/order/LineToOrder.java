package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record LineToOrder(ArticleId article, Quantity quantity) {
  public LineToOrder {
    Assert.notNull("article", article);
    Assert.notNull("quantity", quantity);
  }
}
