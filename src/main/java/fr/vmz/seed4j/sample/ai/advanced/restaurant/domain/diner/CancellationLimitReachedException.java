package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;
import java.time.Year;

public class CancellationLimitReachedException extends RestaurantRuleViolationException {

  public CancellationLimitReachedException(DinerEmail diner, Year year) {
    super("Diner " + diner.get() + " already cancelled 3 bookings in " + year);
  }
}
