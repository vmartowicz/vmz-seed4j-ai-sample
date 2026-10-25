package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

class ClassNotStartedException extends ClassBookingRuleViolationException {

  ClassNotStartedException(ClassBookingId booking) {
    super("The class of booking " + booking.get() + " did not start yet");
  }
}
