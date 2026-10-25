package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

public abstract class ClassBookingRuleViolationException extends RuntimeException {

  protected ClassBookingRuleViolationException(String message) {
    super(message);
  }
}
