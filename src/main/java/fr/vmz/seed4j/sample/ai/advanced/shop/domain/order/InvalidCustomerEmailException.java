package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.InvalidShopValueException;

class InvalidCustomerEmailException extends InvalidShopValueException {

  InvalidCustomerEmailException(String email) {
    super("Invalid email: " + email);
  }
}
