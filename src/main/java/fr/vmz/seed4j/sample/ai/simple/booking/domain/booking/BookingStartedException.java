package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.BookingRuleViolationException;

class BookingStartedException extends BookingRuleViolationException {

  BookingStartedException(BookingId booking) {
    super("This booking has already started: " + booking.get());
  }
}
