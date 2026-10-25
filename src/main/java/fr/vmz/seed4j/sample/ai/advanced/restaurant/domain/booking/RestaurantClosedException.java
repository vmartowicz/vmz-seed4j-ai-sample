package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;
import java.time.LocalDate;

class RestaurantClosedException extends RestaurantRuleViolationException {

  RestaurantClosedException(LocalDate date) {
    super("The restaurant is closed on " + date.getDayOfWeek() + " " + date);
  }
}
