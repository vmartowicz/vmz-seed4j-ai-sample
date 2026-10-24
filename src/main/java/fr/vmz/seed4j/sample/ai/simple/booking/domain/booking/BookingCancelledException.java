package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.BookingRuleViolationException;

class BookingCancelledException extends BookingRuleViolationException {

  BookingCancelledException(BookingId booking) {
    super("This booking is cancelled: " + booking.get());
  }
}
