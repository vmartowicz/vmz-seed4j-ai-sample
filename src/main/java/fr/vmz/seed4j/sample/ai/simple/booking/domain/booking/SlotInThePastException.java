package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.BookingRuleViolationException;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.Slot;

class SlotInThePastException extends BookingRuleViolationException {

  SlotInThePastException(Slot slot) {
    super("Can't book a slot that has already started: " + slot.start());
  }
}
