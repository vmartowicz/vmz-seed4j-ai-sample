package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.EcommerceRuleViolationException;

public class InsufficientStockException extends EcommerceRuleViolationException {

  InsufficientStockException(ProductName product, int quantity) {
    super("Not enough " + product.get() + " in stock for " + quantity + " items");
  }
}
