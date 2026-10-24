package fr.vmz.seed4j.sample.ai.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.clinic.domain.owner.VisitDescription;
import fr.vmz.seed4j.sample.ai.clinic.domain.owner.VisitToSchedule;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestVisitToSchedule(@Nullable LocalDate date, @Nullable String description) {
  VisitToSchedule toDomain() {
    return new VisitToSchedule(date, new VisitDescription(description));
  }
}
