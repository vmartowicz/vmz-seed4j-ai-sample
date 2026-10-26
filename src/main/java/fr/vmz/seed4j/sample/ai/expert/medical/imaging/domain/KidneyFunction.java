package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;

public interface KidneyFunction {
  boolean hasCreatinineSince(PatientId patient, LocalDate since);
}
