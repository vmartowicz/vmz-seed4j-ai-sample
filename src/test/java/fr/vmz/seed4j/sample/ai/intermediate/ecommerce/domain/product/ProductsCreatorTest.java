package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import static fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Price;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class ProductsCreatorTest {

  private final ProductsRepository products = mock(ProductsRepository.class);
  private final ProductsCreator creator = new ProductsCreator(products);

  @Test
  void shouldAddProduct() {
    when(products.all()).thenReturn(List.of(mug(5)));

    var product = creator.add(toCreate("T-shirt"));

    assertThat(product.name()).isEqualTo(new ProductName("T-shirt"));
    verify(products).save(product);
  }

  @Test
  void shouldNotAddTwoProductsWithSameName() {
    when(products.all()).thenReturn(List.of(tShirt(5)));

    assertThatThrownBy(() -> creator.add(toCreate("t-shirt")))
      .isExactlyInstanceOf(DuplicatedProductNameException.class)
      .hasMessageContaining("t-shirt");
    verify(products, never()).save(any());
  }

  private ProductToCreate toCreate(String name) {
    return new ProductToCreate(new ProductName(name), new Price(new BigDecimal("19.99")), new Stock(10));
  }
}
