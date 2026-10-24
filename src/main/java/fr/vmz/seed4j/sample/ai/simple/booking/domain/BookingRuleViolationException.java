package fr.vmz.seed4j.sample.ai.simple.booking.domain;

public abstract class BookingRuleViolationException extends RuntimeException {

  protected BookingRuleViolationException(String message) {
    super(message);
  }
}
