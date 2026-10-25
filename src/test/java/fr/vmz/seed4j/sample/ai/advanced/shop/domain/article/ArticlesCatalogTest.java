package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticlesFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Price;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class ArticlesCatalogTest {

  private final ArticlesRepository articles = mock(ArticlesRepository.class, CALLS_REAL_METHODS);
  private final ArticlesCatalog catalog = new ArticlesCatalog(articles);

  @Test
  void shouldAddArticle() {
    doReturn(List.of(scarf(5))).when(articles).all();

    var article = catalog.add(toAdd("White T-shirt M"));

    assertThat(article.stock()).isEqualTo(new Stock(10));
    verify(articles).save(article);
  }

  @Test
  void shouldNotAddTwoArticlesWithTheSameName() {
    doReturn(List.of(tShirt(5))).when(articles).all();

    assertThatThrownBy(() -> catalog.add(toAdd("white t-shirt m")))
      .isExactlyInstanceOf(DuplicatedArticleNameException.class)
      .hasMessageContaining("white t-shirt m");
    verify(articles, never()).save(any());
  }

  @Test
  void shouldRestockArticle() {
    var article = tShirt(2);
    doReturn(Optional.of(article)).when(articles).get(article.id());

    catalog.restock(article.id(), new RestockQuantity(10));

    assertThat(article.stock()).isEqualTo(new Stock(12));
    verify(articles).save(article);
  }

  @Test
  void shouldNotRestockUnknownArticle() {
    var unknown = ArticleId.newId();
    doReturn(Optional.empty()).when(articles).get(unknown);

    assertThatThrownBy(() -> catalog.restock(unknown, new RestockQuantity(1)))
      .isExactlyInstanceOf(UnknownArticleException.class)
      .hasMessageContaining(unknown.get().toString());
  }

  @Test
  void shouldListArticlesInStockAlertFromLowestStock() {
    var scarf = scarf(4);
    var belt = article("Leather belt", "25", 0);
    var bag = article("Bag", "60", 4);
    doReturn(List.of(scarf, tShirt(5), belt, bag)).when(articles).all();

    assertThat(catalog.lowStock()).containsExactly(belt, bag, scarf);
  }

  private ArticleToAdd toAdd(String name) {
    return new ArticleToAdd(new ArticleName(name), new Price(new BigDecimal("20")), new Stock(10));
  }
}
