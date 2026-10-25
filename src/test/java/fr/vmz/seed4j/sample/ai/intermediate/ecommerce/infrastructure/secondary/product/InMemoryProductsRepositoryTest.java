package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.secondary.product;

import static fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryProductsRepositoryTest {

  private final InMemoryProductsRepository products = new InMemoryProductsRepository();

  @Test
  void shouldGetSavedProduct() {
    var product = tShirt(10);
    products.save(product);

    assertThat(products.get(product.id())).contains(product);
    assertThat(products.get(ProductId.newId())).isEmpty();
  }

  @Test
  void shouldListProductsByName() {
    var tShirt = tShirt(10);
    var mug = mug(5);
    var bag = product("bag", "30", 1);
    products.save(tShirt);
    products.save(mug);
    products.save(bag);

    assertThat(products.all()).containsExactly(bag, mug, tShirt);
  }
}
