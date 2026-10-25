package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.Duration;
import java.time.LocalDateTime;

public record TeachingSlot(Gym gym, LocalDateTime start, LocalDateTime end) {
  private static final int SAME_GYM_GAP_MINUTES = 15;
  private static final int OTHER_GYM_GAP_MINUTES = 30;

  public long minutes() {
    return Duration.between(start, end).toMinutes();
  }

  public boolean overlaps(TeachingSlot other) {
    return start.isBefore(other.end()) && other.start().isBefore(end);
  }

  public boolean isTooCloseTo(TeachingSlot other) {
    var gap = gym == other.gym() ? SAME_GYM_GAP_MINUTES : OTHER_GYM_GAP_MINUTES;

    return start.isBefore(other.end().plusMinutes(gap)) && other.start().isBefore(end.plusMinutes(gap));
  }
}
