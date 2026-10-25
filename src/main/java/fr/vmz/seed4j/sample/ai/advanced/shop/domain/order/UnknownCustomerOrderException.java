package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.UnknownShopElementException;

class UnknownCustomerOrderException extends UnknownShopElementException {

  UnknownCustomerOrderException(CustomerOrderId order) {
    super("Unknown order " + order.get());
  }
}
