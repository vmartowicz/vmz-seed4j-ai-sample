package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.UnknownRestaurantElementException;

public class UnknownDinerException extends UnknownRestaurantElementException {

  public UnknownDinerException(DinerEmail diner) {
    super("Unknown diner " + diner.get());
  }
}
