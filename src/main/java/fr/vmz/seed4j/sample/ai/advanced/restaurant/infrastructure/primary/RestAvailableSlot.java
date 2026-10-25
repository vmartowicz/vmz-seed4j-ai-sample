package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.AvailableSlot;
import java.time.LocalTime;
import java.util.List;

record RestAvailableSlot(LocalTime time, List<RestTable> tables) {
  static RestAvailableSlot from(AvailableSlot slot) {
    return new RestAvailableSlot(slot.arrival(), RestTable.from(slot.tables()));
  }
}
