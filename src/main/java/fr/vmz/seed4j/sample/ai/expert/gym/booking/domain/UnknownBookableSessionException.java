package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

class UnknownBookableSessionException extends UnknownClassBookingElementException {

  UnknownBookableSessionException(SessionId session) {
    super("Unknown session " + session.get());
  }
}
