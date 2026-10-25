package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import static fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Price;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class ProductTest {

  @Test
  void shouldCreateProduct() {
    var product = tShirt(10);

    assertThat(product.id()).isNotNull();
    assertThat(product.name()).isEqualTo(new ProductName("T-shirt"));
    assertThat(product.price()).isEqualTo(new Price(new BigDecimal("19.99")));
    assertThat(product.stock()).isEqualTo(new Stock(10));
  }

  @Test
  void shouldReserveWholeStock() {
    var product = tShirt(10);

    product.reserve(10);

    assertThat(product.stock()).isEqualTo(new Stock(0));
  }

  @Test
  void shouldNotReserveMoreThanStock() {
    var product = tShirt(10);

    assertThatThrownBy(() -> product.reserve(11))
      .isExactlyInstanceOf(InsufficientStockException.class)
      .hasMessageContaining("T-shirt");
    assertThat(product.stock()).isEqualTo(new Stock(10));
  }

  @Test
  void shouldReleaseStock() {
    var product = tShirt(10);

    product.release(3);

    assertThat(product.stock()).isEqualTo(new Stock(13));
  }
}
