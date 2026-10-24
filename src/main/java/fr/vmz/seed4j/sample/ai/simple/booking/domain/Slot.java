package fr.vmz.seed4j.sample.ai.simple.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;

public record Slot(LocalDateTime start, LocalDateTime end) {
  public Slot {
    Assert.notNull("start", start);
    Assert.notNull("end", end);
  }

  public boolean overlaps(Slot other) {
    return start.isBefore(other.end()) && other.start().isBefore(end);
  }
}
