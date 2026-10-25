package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;

public class BlacklistedDinerException extends RestaurantRuleViolationException {

  public BlacklistedDinerException(DinerEmail diner) {
    super("Diner " + diner.get() + " is blacklisted after 3 no-shows");
  }
}
