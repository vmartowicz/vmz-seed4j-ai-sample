package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;

public interface CareRegistry {
  boolean patientExists(PatientId patient);

  boolean doctorExists(DoctorId doctor);
}
