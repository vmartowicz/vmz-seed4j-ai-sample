package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public final class RoomPresence {

  private final UUID id;
  private final BadgeId badge;
  private final Room room;
  private final LocalDateTime enteredAt;
  private final @Nullable RoomSession session;
  private @Nullable LocalDateTime exitedAt;

  public RoomPresence(BadgeId badge, Room room, LocalDateTime enteredAt, @Nullable RoomSession session) {
    id = UUID.randomUUID();
    this.badge = badge;
    this.room = room;
    this.enteredAt = enteredAt;
    this.session = session;
  }

  public void exit(LocalDateTime at) {
    exitedAt = at;
  }

  public boolean isOpen() {
    return exitedAt == null;
  }

  public UUID id() {
    return id;
  }

  public BadgeId badge() {
    return badge;
  }

  public Room room() {
    return room;
  }

  public LocalDateTime enteredAt() {
    return enteredAt;
  }

  public Optional<RoomSession> session() {
    return Optional.ofNullable(session);
  }

  public Optional<LocalDateTime> exitedAt() {
    return Optional.ofNullable(exitedAt);
  }
}
