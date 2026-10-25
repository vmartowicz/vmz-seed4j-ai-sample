package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.secondary.article;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.Article;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticlesRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryArticlesRepository implements ArticlesRepository {

  private static final Comparator<Article> BY_NAME = Comparator.comparing(article -> article.name().get(), String.CASE_INSENSITIVE_ORDER);

  private final Map<ArticleId, Article> articles = new ConcurrentHashMap<>();

  @Override
  public void save(Article article) {
    articles.put(article.id(), article);
  }

  @Override
  public Optional<Article> get(ArticleId article) {
    return Optional.ofNullable(articles.get(article));
  }

  @Override
  public List<Article> all() {
    return articles.values().stream().sorted(BY_NAME).toList();
  }
}
