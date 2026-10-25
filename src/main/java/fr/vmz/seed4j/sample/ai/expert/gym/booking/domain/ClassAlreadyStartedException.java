package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

class ClassAlreadyStartedException extends ClassBookingRuleViolationException {

  ClassAlreadyStartedException(ClassBookingId booking) {
    super("The class of booking " + booking.get() + " already started");
  }
}
