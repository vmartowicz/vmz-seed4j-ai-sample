package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Comparator;
import java.util.List;

public class ArticlesCatalog {

  private static final Comparator<Article> LOWEST_STOCK_FIRST = Comparator.comparing((Article article) -> article.stock().get()).thenComparing(
    article -> article.name().get(),
    String.CASE_INSENSITIVE_ORDER
  );

  private final ArticlesRepository articles;

  public ArticlesCatalog(ArticlesRepository articles) {
    this.articles = articles;
  }

  public Article add(ArticleToAdd toAdd) {
    Assert.notNull("toAdd", toAdd);

    if (articles.all().stream().anyMatch(article -> article.name().sameAs(toAdd.name()))) {
      throw new DuplicatedArticleNameException(toAdd.name());
    }

    var article = toAdd.add();
    articles.save(article);

    return article;
  }

  public Article restock(ArticleId articleId, RestockQuantity quantity) {
    var article = articles.article(articleId);
    article.restock(quantity);
    articles.save(article);

    return article;
  }

  public List<Article> lowStock() {
    return articles
      .all()
      .stream()
      .filter(article -> article.stock().isLow())
      .sorted(LOWEST_STOCK_FIRST)
      .toList();
  }
}
