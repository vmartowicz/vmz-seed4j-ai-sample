package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.application.ProductsApplicationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ecommerce/products")
class ProductsResource {

  private final ProductsApplicationService products;

  ProductsResource(ProductsApplicationService products) {
    this.products = products;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestProduct add(@RequestBody RestProductToCreate product) {
    return RestProduct.from(products.add(product.toDomain()));
  }

  @GetMapping
  List<RestProduct> list() {
    return products.all().stream().map(RestProduct::from).toList();
  }
}
