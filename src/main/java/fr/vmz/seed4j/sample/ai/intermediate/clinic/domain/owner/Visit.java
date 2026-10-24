package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public record Visit(VisitId id, LocalDate date, VisitDescription description) {
  public Visit {
    Assert.notNull("id", id);
    Assert.notNull("date", date);
    Assert.notNull("description", description);
  }
}
