package fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.InvalidBookingValueException;
import java.time.LocalDateTime;

class InvalidSlotException extends InvalidBookingValueException {

  InvalidSlotException(LocalDateTime start) {
    super("No slot starts at " + start + ": slots follow the service duration, Monday to Saturday from 09:00 to 18:00");
  }
}
