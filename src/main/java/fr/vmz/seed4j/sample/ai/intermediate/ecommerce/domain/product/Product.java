package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Price;

public final class Product {

  private final ProductId id;
  private final ProductName name;
  private final Price price;
  private Stock stock;

  Product(ProductId id, ProductToCreate product) {
    this.id = id;
    name = product.name();
    price = product.price();
    stock = product.stock();
  }

  public void assertAvailable(int quantity) {
    if (!stock.covers(quantity)) {
      throw new InsufficientStockException(name, quantity);
    }
  }

  public void reserve(int quantity) {
    assertAvailable(quantity);

    stock = stock.minus(quantity);
  }

  public void release(int quantity) {
    stock = stock.plus(quantity);
  }

  public ProductId id() {
    return id;
  }

  public ProductName name() {
    return name;
  }

  public Price price() {
    return price;
  }

  public Stock stock() {
    return stock;
  }
}
