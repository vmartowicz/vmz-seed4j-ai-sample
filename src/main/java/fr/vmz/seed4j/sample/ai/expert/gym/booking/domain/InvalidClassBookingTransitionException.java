package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

class InvalidClassBookingTransitionException extends ClassBookingRuleViolationException {

  InvalidClassBookingTransitionException(ClassBookingId booking, ClassBookingStatus status, String action) {
    super("Can not " + action + " booking " + booking.get() + " which is " + status);
  }
}
