package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain;

public abstract class UnknownRestaurantElementException extends RuntimeException {

  protected UnknownRestaurantElementException(String message) {
    super(message);
  }
}
