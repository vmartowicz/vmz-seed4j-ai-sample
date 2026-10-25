package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository {
  void save(Product product);

  Optional<Product> get(ProductId product);

  List<Product> all();

  default Product product(ProductId product) {
    return get(product).orElseThrow(() -> new UnknownProductException(product));
  }
}
