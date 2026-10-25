package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain;

public abstract class InvalidRestaurantValueException extends RuntimeException {

  protected InvalidRestaurantValueException(String message) {
    super(message);
  }
}
