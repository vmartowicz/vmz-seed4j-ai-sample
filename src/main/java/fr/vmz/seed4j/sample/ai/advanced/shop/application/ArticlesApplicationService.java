package fr.vmz.seed4j.sample.ai.advanced.shop.application;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.Article;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleToAdd;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticlesCatalog;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticlesRepository;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.RestockQuantity;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ArticlesApplicationService {

  private final ArticlesRepository articles;
  private final ArticlesCatalog catalog;

  public ArticlesApplicationService(ArticlesRepository articles) {
    this.articles = articles;
    catalog = new ArticlesCatalog(articles);
  }

  public Article add(ArticleToAdd article) {
    return catalog.add(article);
  }

  public List<Article> all() {
    return articles.all();
  }

  public Article restock(ArticleId article, RestockQuantity quantity) {
    return catalog.restock(article, quantity);
  }

  public List<Article> lowStock() {
    return catalog.lowStock();
  }
}
