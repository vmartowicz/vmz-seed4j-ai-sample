package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.UnknownRestaurantElementException;

class UnknownTableBookingException extends UnknownRestaurantElementException {

  UnknownTableBookingException(TableBookingId booking) {
    super("Unknown booking " + booking.get());
  }
}
