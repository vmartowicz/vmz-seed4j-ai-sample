package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.UnknownEcommerceElementException;

class UnknownOrderException extends UnknownEcommerceElementException {

  UnknownOrderException(OrderId order) {
    super("Unknown order " + order.get());
  }
}
