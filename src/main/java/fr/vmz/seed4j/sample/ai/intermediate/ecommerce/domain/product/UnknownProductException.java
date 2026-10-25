package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.UnknownEcommerceElementException;

public class UnknownProductException extends UnknownEcommerceElementException {

  UnknownProductException(ProductId product) {
    super("Unknown product " + product.get());
  }
}
