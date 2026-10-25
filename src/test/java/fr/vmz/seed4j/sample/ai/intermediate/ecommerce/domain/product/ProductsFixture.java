package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Price;
import java.math.BigDecimal;

public final class ProductsFixture {

  private ProductsFixture() {}

  public static Product tShirt(int stock) {
    return product("T-shirt", "19.99", stock);
  }

  public static Product mug(int stock) {
    return product("Mug", "8.50", stock);
  }

  public static Product product(String name, String price, int stock) {
    return new ProductToCreate(new ProductName(name), new Price(new BigDecimal(price)), new Stock(stock)).create();
  }
}
