package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.secondary.records;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.Implants;
import fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary.JavaMedicalRecords;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import org.springframework.stereotype.Component;

@Component
class ImagingImplants implements Implants {

  private final JavaMedicalRecords records;

  ImagingImplants(JavaMedicalRecords records) {
    this.records = records;
  }

  @Override
  public boolean hasPacemaker(PatientId patient) {
    return records.hasPacemaker(patient.get());
  }
}
