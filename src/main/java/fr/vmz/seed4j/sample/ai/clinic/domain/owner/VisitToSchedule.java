package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public record VisitToSchedule(LocalDate date, VisitDescription description) {
  public VisitToSchedule {
    Assert.notNull("date", date);
    Assert.notNull("description", description);
  }
}
