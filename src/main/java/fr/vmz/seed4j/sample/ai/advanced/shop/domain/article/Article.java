package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Price;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public final class Article {

  private final ArticleId id;
  private final ArticleName name;
  private final Price price;
  private Stock stock;

  Article(ArticleId id, ArticleToAdd article) {
    this.id = id;
    name = article.name();
    price = article.price();
    stock = article.stock();
  }

  public void assertAvailable(int quantity) {
    if (!stock.covers(quantity)) {
      throw new InsufficientStockException(name, quantity);
    }
  }

  public void reserve(int quantity) {
    assertAvailable(quantity);

    stock = stock.minus(quantity);
  }

  public void release(int quantity) {
    stock = stock.plus(quantity);
  }

  public void restock(RestockQuantity quantity) {
    Assert.notNull("quantity", quantity);

    stock = stock.plus(quantity.get());
  }

  public ArticleId id() {
    return id;
  }

  public ArticleName name() {
    return name;
  }

  public Price price() {
    return price;
  }

  public Stock stock() {
    return stock;
  }
}
