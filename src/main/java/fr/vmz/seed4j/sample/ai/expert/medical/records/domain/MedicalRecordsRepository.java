package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Optional;

public interface MedicalRecordsRepository {
  void save(MedicalRecord record);

  Optional<MedicalRecord> get(PatientId patient);
}
