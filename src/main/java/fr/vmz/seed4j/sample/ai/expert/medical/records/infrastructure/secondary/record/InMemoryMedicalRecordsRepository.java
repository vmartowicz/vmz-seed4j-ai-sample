package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.secondary.record;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.MedicalRecord;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.MedicalRecordsRepository;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryMedicalRecordsRepository implements MedicalRecordsRepository {

  private final Map<PatientId, MedicalRecord> records = new ConcurrentHashMap<>();

  @Override
  public void save(MedicalRecord record) {
    records.put(record.patient(), record);
  }

  @Override
  public Optional<MedicalRecord> get(PatientId patient) {
    return Optional.ofNullable(records.get(patient));
  }
}
