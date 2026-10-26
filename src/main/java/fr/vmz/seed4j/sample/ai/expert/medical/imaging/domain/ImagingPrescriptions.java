package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import java.util.Optional;
import java.util.UUID;

public interface ImagingPrescriptions {
  Optional<ImagingPrescription> prescriptionOf(UUID prescription);
}
