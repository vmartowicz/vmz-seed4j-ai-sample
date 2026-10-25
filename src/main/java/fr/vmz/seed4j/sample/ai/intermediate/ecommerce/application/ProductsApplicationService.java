package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.application;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.Product;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductToCreate;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductsCreator;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductsRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductsApplicationService {

  private final ProductsRepository products;
  private final ProductsCreator creator;

  public ProductsApplicationService(ProductsRepository products) {
    this.products = products;
    creator = new ProductsCreator(products);
  }

  public Product add(ProductToCreate product) {
    return creator.add(product);
  }

  public List<Product> all() {
    return products.all();
  }
}
