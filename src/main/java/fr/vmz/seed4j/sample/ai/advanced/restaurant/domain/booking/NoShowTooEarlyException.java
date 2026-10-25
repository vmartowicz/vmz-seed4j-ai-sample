package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;
import java.time.LocalDateTime;

class NoShowTooEarlyException extends RestaurantRuleViolationException {

  NoShowTooEarlyException(TableBookingId booking, LocalDateTime at) {
    super("Booking " + booking.get() + " is expected at " + at);
  }
}
