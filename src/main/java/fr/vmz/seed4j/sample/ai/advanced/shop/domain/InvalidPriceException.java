package fr.vmz.seed4j.sample.ai.advanced.shop.domain;

import java.math.BigDecimal;

class InvalidPriceException extends InvalidShopValueException {

  InvalidPriceException(BigDecimal price) {
    super("A price has at most 2 decimals, got: " + price.toPlainString());
  }
}
