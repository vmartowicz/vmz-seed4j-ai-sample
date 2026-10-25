package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;

class InvalidBookingTransitionException extends RestaurantRuleViolationException {

  InvalidBookingTransitionException(TableBookingId booking, BookingStatus status, String action) {
    super("Can not " + action + " booking " + booking.get() + " which is " + status);
  }
}
