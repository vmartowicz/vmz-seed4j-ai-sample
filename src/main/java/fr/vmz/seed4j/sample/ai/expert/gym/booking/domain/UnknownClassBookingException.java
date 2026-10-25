package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

class UnknownClassBookingException extends UnknownClassBookingElementException {

  UnknownClassBookingException(ClassBookingId booking) {
    super("Unknown booking " + booking.get());
  }
}
