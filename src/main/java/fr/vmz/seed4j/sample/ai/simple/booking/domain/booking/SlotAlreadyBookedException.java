package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.BookingRuleViolationException;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.Slot;

class SlotAlreadyBookedException extends BookingRuleViolationException {

  SlotAlreadyBookedException(Slot slot) {
    super("The slot starting at " + slot.start() + " overlaps another booking");
  }
}
