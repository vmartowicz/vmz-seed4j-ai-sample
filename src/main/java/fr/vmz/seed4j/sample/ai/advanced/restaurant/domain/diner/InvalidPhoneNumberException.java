package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.InvalidRestaurantValueException;

class InvalidPhoneNumberException extends InvalidRestaurantValueException {

  InvalidPhoneNumberException(String number) {
    super("Invalid French phone number (expected 0X XX XX XX XX): " + number);
  }
}
