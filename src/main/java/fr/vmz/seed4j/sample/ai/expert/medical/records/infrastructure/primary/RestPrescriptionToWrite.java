package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionItem;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

record RestPrescriptionToWrite(@Nullable List<RestPrescriptionItemToWrite> items) {
  List<PrescriptionItem> toDomain() {
    return Optional.ofNullable(items).orElse(List.of()).stream().map(RestPrescriptionItemToWrite::toDomain).toList();
  }
}
