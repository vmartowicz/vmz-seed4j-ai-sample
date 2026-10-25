package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RefusalReason;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomEntry;
import org.jspecify.annotations.Nullable;

record RestRoomEntry(boolean accepted, @Nullable RefusalReason refusal, @Nullable String message) {
  static RestRoomEntry from(RoomEntry entry) {
    return new RestRoomEntry(entry.accepted(), entry.refusal().orElse(null), entry.refusal().map(RefusalReason::message).orElse(null));
  }
}
