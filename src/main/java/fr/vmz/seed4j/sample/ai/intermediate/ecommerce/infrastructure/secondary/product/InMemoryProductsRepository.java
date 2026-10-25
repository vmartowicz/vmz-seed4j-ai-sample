package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.secondary.product;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.Product;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductId;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductsRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryProductsRepository implements ProductsRepository {

  private static final Comparator<Product> BY_NAME = Comparator.comparing(product -> product.name().get(), String.CASE_INSENSITIVE_ORDER);

  private final Map<ProductId, Product> products = new ConcurrentHashMap<>();

  @Override
  public void save(Product product) {
    products.put(product.id(), product);
  }

  @Override
  public Optional<Product> get(ProductId product) {
    return Optional.ofNullable(products.get(product));
  }

  @Override
  public List<Product> all() {
    return products.values().stream().sorted(BY_NAME).toList();
  }
}
