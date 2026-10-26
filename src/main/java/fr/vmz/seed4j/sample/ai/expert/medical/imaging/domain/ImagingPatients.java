package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Optional;

public interface ImagingPatients {
  Optional<ImagingPatient> profileOf(PatientId patient);
}
