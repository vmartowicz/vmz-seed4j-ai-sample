package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.InvalidBookingValueException;

class InvalidMobilePhoneException extends InvalidBookingValueException {

  InvalidMobilePhoneException(String phone) {
    super("A mobile phone has 10 digits and starts with 06 or 07, got: " + phone);
  }
}
