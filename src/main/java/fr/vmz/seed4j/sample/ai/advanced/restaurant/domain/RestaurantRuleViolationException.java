package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain;

public abstract class RestaurantRuleViolationException extends RuntimeException {

  protected RestaurantRuleViolationException(String message) {
    super(message);
  }
}
