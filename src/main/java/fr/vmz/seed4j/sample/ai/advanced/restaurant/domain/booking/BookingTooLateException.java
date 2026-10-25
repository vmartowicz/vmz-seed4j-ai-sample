package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;

class BookingTooLateException extends RestaurantRuleViolationException {

  BookingTooLateException(Slot slot) {
    super("Bookings close 2 hours before the service, too late for " + slot.at());
  }
}
