package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;
import java.time.LocalDate;

class BookingTooFarException extends RestaurantRuleViolationException {

  BookingTooFarException(LocalDate date) {
    super("Bookings open 30 days in advance, " + date + " is too far");
  }
}
