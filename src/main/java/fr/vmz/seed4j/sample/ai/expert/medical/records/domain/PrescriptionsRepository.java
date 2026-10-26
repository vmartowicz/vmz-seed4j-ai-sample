package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import java.util.Optional;

public interface PrescriptionsRepository {
  void save(Prescription prescription);

  Optional<Prescription> get(PrescriptionId prescription);

  default Prescription prescriptionOf(PrescriptionId prescription) {
    return get(prescription).orElseThrow(() -> new UnknownPrescriptionException(prescription));
  }
}
