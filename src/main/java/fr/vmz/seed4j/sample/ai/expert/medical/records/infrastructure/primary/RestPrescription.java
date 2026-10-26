package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.Prescription;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

record RestPrescription(UUID id, UUID patientId, UUID prescriberId, LocalDate issuedOn, List<RestPrescriptionItem> items) {
  static RestPrescription from(Prescription prescription) {
    return new RestPrescription(
      prescription.id().get(),
      prescription.patient().get(),
      prescription.prescriber().get(),
      prescription.issuedOn(),
      prescription
        .items()
        .stream()
        .map(item -> new RestPrescriptionItem(item.kind(), item.code(), prescription.validUntil(item.kind())))
        .toList()
    );
  }
}
