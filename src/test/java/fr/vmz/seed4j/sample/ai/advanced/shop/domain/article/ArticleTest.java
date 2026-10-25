package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticlesFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Price;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class ArticleTest {

  @Test
  void shouldAddArticle() {
    var article = tShirt(10);

    assertThat(article.id()).isNotNull();
    assertThat(article.name()).isEqualTo(new ArticleName("White T-shirt M"));
    assertThat(article.price()).isEqualTo(new Price(new BigDecimal("20")));
    assertThat(article.stock()).isEqualTo(new Stock(10));
  }

  @Test
  void shouldReserveWholeStock() {
    var article = tShirt(10);

    article.reserve(10);

    assertThat(article.stock()).isEqualTo(new Stock(0));
  }

  @Test
  void shouldNotReserveMoreThanStock() {
    var article = tShirt(10);

    assertThatThrownBy(() -> article.reserve(11)).isExactlyInstanceOf(InsufficientStockException.class).hasMessageContaining("White T-shirt M");
    assertThat(article.stock()).isEqualTo(new Stock(10));
  }

  @Test
  void shouldReleaseAndRestockArticle() {
    var article = tShirt(10);

    article.release(2);
    article.restock(new RestockQuantity(8));

    assertThat(article.stock()).isEqualTo(new Stock(20));
  }

  @Test
  void shouldNotRestockNothing() {
    assertThatThrownBy(() -> new RestockQuantity(0)).isExactlyInstanceOf(NumberValueTooLowException.class);
  }
}
