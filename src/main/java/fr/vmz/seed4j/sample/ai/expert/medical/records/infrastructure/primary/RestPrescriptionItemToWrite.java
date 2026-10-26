package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ItemKind;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionItem;
import org.jspecify.annotations.Nullable;

record RestPrescriptionItemToWrite(@Nullable ItemKind kind, @Nullable String code) {
  PrescriptionItem toDomain() {
    return new PrescriptionItem(kind, code);
  }
}
