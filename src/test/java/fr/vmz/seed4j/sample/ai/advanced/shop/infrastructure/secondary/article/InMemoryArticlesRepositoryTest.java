package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.secondary.article;

import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticlesFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryArticlesRepositoryTest {

  private final InMemoryArticlesRepository articles = new InMemoryArticlesRepository();

  @Test
  void shouldGetSavedArticle() {
    var article = tShirt(10);
    articles.save(article);

    assertThat(articles.get(article.id())).contains(article);
    assertThat(articles.get(ArticleId.newId())).isEmpty();
  }

  @Test
  void shouldListArticlesByName() {
    var tShirt = tShirt(10);
    var scarf = scarf(5);
    var bag = article("bag", "60", 1);
    articles.save(tShirt);
    articles.save(scarf);
    articles.save(bag);

    assertThat(articles.all()).containsExactly(bag, scarf, tShirt);
  }
}
