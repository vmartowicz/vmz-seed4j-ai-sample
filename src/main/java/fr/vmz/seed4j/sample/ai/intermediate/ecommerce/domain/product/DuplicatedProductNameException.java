package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.EcommerceRuleViolationException;

class DuplicatedProductNameException extends EcommerceRuleViolationException {

  DuplicatedProductNameException(ProductName name) {
    super("A product is already named " + name.get());
  }
}
