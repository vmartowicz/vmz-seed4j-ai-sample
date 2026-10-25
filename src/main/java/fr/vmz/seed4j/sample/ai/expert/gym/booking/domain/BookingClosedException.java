package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

class BookingClosedException extends ClassBookingRuleViolationException {

  BookingClosedException(BookableSession session) {
    super("Bookings close 1 hour before the class of " + session.start());
  }
}
