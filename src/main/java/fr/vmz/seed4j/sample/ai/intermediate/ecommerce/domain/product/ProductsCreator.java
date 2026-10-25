package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public class ProductsCreator {

  private final ProductsRepository products;

  public ProductsCreator(ProductsRepository products) {
    this.products = products;
  }

  public Product add(ProductToCreate toCreate) {
    Assert.notNull("toCreate", toCreate);

    if (products.all().stream().anyMatch(product -> product.name().sameAs(toCreate.name()))) {
      throw new DuplicatedProductNameException(toCreate.name());
    }

    var product = toCreate.create();
    products.save(product);

    return product;
  }
}
