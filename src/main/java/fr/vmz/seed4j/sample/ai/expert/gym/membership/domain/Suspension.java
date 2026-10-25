package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.time.Year;

public record Suspension(LocalDate start, int months) {
  public Suspension {
    Assert.notNull("start", start);
    Assert.field("months", months).min(1).max(3);
  }

  public LocalDate end() {
    return start.plusMonths(months);
  }

  public boolean covers(LocalDate date) {
    return !date.isBefore(start) && date.isBefore(end());
  }

  Year year() {
    return Year.from(start);
  }
}
