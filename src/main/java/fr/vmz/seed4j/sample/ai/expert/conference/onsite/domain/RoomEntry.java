package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import java.util.Optional;

public record RoomEntry(Optional<RefusalReason> refusal) {
  public static RoomEntry welcome() {
    return new RoomEntry(Optional.empty());
  }

  public static RoomEntry refused(RefusalReason reason) {
    return new RoomEntry(Optional.of(reason));
  }

  public boolean accepted() {
    return refusal.isEmpty();
  }
}
