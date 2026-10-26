package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.application.MedicalRecordsApplicationService;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaMedicalRecords {

  private final MedicalRecordsApplicationService records;

  public JavaMedicalRecords(MedicalRecordsApplicationService records) {
    this.records = records;
  }

  public boolean hasPacemaker(UUID patient) {
    return records.hasPacemaker(new PatientId(patient));
  }
}
