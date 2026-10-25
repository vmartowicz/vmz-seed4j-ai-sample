package fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.Slot;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDate;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestSessionToPlace(@Nullable UUID talkId, @Nullable Room room, @Nullable LocalDate day, int slot) {
  Slot toSlot() {
    return new Slot(day, slot);
  }
}
