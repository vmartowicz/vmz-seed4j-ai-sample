package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

class BookingNotOpenException extends ClassBookingRuleViolationException {

  BookingNotOpenException(BookableSession session) {
    super("Bookings open 7 days before the class of " + session.start());
  }
}
