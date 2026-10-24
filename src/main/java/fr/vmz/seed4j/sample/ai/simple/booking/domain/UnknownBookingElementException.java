package fr.vmz.seed4j.sample.ai.simple.booking.domain;

public abstract class UnknownBookingElementException extends RuntimeException {

  protected UnknownBookingElementException(String message) {
    super(message);
  }
}
