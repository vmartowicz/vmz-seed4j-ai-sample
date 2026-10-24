package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.UnknownBookingElementException;

class UnknownBookingException extends UnknownBookingElementException {

  UnknownBookingException(BookingId booking) {
    super("Unknown booking " + booking.get());
  }
}
