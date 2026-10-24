package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.Visit;
import java.time.LocalDate;
import java.util.UUID;

record RestVisit(UUID id, LocalDate date, String description) {
  static RestVisit from(Visit visit) {
    return new RestVisit(visit.id().get(), visit.date(), visit.description().get());
  }
}
