package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.util.Optional;

public interface Practitioners {
  Optional<Tariff> tariffOf(DoctorId doctor);
}
