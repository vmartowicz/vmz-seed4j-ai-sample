package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.InvalidRestaurantValueException;
import java.time.LocalTime;

class InvalidArrivalTimeException extends InvalidRestaurantValueException {

  InvalidArrivalTimeException(LocalTime time) {
    super("No service accepts arrivals at " + time + " (lunch 12:00-13:30, dinner 19:00-21:30, every 30 minutes)");
  }
}
