package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public final class Presence {

  private static final int MAX_HOURS_INSIDE = 4;

  private final UUID id;
  private final MemberId member;
  private final Gym gym;
  private final LocalDateTime enteredAt;
  private @Nullable LocalDateTime exitedAt;

  public Presence(MemberId member, Gym gym, LocalDateTime enteredAt) {
    Assert.notNull("member", member);
    Assert.notNull("gym", gym);
    Assert.notNull("enteredAt", enteredAt);

    id = UUID.randomUUID();
    this.member = member;
    this.gym = gym;
    this.enteredAt = enteredAt;
  }

  public void exit(LocalDateTime at) {
    exitedAt = at;
  }

  public boolean isOpen() {
    return exitedAt == null;
  }

  public boolean isExpiredAt(LocalDateTime now) {
    return !now.isBefore(enteredAt.plusHours(MAX_HOURS_INSIDE));
  }

  public UUID id() {
    return id;
  }

  public MemberId member() {
    return member;
  }

  public Gym gym() {
    return gym;
  }

  public LocalDateTime enteredAt() {
    return enteredAt;
  }

  public Optional<LocalDateTime> exitedAt() {
    return Optional.ofNullable(exitedAt);
  }
}
