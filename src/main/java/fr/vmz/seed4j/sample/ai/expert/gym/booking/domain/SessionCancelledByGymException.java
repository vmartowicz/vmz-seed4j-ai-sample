package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

class SessionCancelledByGymException extends ClassBookingRuleViolationException {

  SessionCancelledByGymException(SessionId session) {
    super("Session " + session.get() + " was cancelled by the gym");
  }
}
