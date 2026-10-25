package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;
import java.time.LocalDate;

class NotBookingDayException extends RestaurantRuleViolationException {

  NotBookingDayException(TableBookingId booking, LocalDate date) {
    super("Booking " + booking.get() + " is on " + date);
  }
}
