package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain;

import java.math.BigDecimal;

class InvalidPriceException extends InvalidEcommerceValueException {

  InvalidPriceException(BigDecimal price) {
    super("A price has at most 2 decimals, got: " + price.toPlainString());
  }
}
