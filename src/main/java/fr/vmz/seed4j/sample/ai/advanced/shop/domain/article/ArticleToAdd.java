package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Price;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record ArticleToAdd(ArticleName name, Price price, Stock stock) {
  public ArticleToAdd {
    Assert.notNull("name", name);
    Assert.notNull("price", price);
    Assert.notNull("stock", stock);
  }

  public Article add() {
    return new Article(ArticleId.newId(), this);
  }
}
