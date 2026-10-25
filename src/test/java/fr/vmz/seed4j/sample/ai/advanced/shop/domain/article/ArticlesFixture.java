package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Price;
import java.math.BigDecimal;

public final class ArticlesFixture {

  private ArticlesFixture() {}

  public static Article tShirt(int stock) {
    return article("White T-shirt M", "20.00", stock);
  }

  public static Article scarf(int stock) {
    return article("Silk scarf", "35.00", stock);
  }

  public static Article article(String name, String price, int stock) {
    return new ArticleToAdd(new ArticleName(name), new Price(new BigDecimal(price)), new Stock(stock)).add();
  }
}
