package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;

public interface Implants {
  boolean hasPacemaker(PatientId patient);
}
