package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.Addendum;
import java.time.LocalDateTime;
import java.util.UUID;

record RestAddendum(UUID doctorId, String text, LocalDateTime writtenAt) {
  static RestAddendum from(Addendum addendum) {
    return new RestAddendum(addendum.author().get(), addendum.text(), addendum.writtenAt());
  }
}
