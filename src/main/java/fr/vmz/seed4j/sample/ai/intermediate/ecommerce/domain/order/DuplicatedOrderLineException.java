package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.InvalidEcommerceValueException;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductId;

class DuplicatedOrderLineException extends InvalidEcommerceValueException {

  DuplicatedOrderLineException(ProductId product) {
    super("Product " + product.get() + " appears in several lines of the order");
  }
}
