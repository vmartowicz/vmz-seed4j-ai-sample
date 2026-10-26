package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Slot;
import java.time.LocalDateTime;

record RestSlot(LocalDateTime start, LocalDateTime end) {
  static RestSlot from(Slot slot) {
    return new RestSlot(slot.start(), slot.end());
  }
}
