package fr.vmz.seed4j.sample.ai.simple.booking.domain;

public abstract class InvalidBookingValueException extends RuntimeException {

  protected InvalidBookingValueException(String message) {
    super(message);
  }
}
