package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.InvalidRestaurantValueException;

class InvalidDinerEmailException extends InvalidRestaurantValueException {

  InvalidDinerEmailException(String email) {
    super("Invalid email: " + email);
  }
}
