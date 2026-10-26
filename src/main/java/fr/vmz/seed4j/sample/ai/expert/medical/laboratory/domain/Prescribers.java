package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.util.Optional;

public interface Prescribers {
  Optional<DoctorId> onCallReplacing(DoctorId prescriber);
}
