package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import java.util.Optional;

public record Entry(Optional<RefusalReason> refusal) {
  public static Entry welcome() {
    return new Entry(Optional.empty());
  }

  public static Entry refused(RefusalReason reason) {
    return new Entry(Optional.of(reason));
  }

  public boolean accepted() {
    return refusal.isEmpty();
  }
}
