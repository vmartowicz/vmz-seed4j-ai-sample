package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Optional;

public interface Insureds {
  Optional<Insurance> insuranceOf(PatientId patient);
}
