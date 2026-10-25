package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.InvalidEcommerceValueException;

class InvalidCustomerEmailException extends InvalidEcommerceValueException {

  InvalidCustomerEmailException(String email) {
    super("Invalid email: " + email);
  }
}
