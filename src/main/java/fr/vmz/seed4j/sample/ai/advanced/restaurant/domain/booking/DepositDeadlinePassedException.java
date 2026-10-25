package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;
import java.time.LocalDateTime;

class DepositDeadlinePassedException extends RestaurantRuleViolationException {

  DepositDeadlinePassedException(TableBookingId booking, LocalDateTime dueBy) {
    super("The deposit of booking " + booking.get() + " was due by " + dueBy);
  }
}
