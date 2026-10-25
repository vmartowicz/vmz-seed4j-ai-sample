package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import java.util.List;
import java.util.Optional;

public interface ArticlesRepository {
  void save(Article article);

  Optional<Article> get(ArticleId article);

  List<Article> all();

  default Article article(ArticleId article) {
    return get(article).orElseThrow(() -> new UnknownArticleException(article));
  }
}
