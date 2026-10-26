package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import java.util.Optional;
import java.util.UUID;

public interface LabPrescriptions {
  Optional<LabPrescription> prescriptionOf(UUID prescription);
}
