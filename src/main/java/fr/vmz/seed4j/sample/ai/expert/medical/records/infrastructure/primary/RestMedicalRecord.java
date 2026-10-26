package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.MedicalRecord;
import java.util.List;
import java.util.UUID;

record RestMedicalRecord(UUID patientId, List<String> allergies, List<String> history, List<String> treatments, boolean pacemaker) {
  static RestMedicalRecord from(MedicalRecord record) {
    return new RestMedicalRecord(record.patient().get(), record.allergies(), record.history(), record.treatments(), record.hasPacemaker());
  }
}
