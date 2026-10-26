package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.secondary.records;

import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabPrescription;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabPrescriptions;
import fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary.JavaPrescriptions;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class LaboratoryPrescriptions implements LabPrescriptions {

  private final JavaPrescriptions prescriptions;

  LaboratoryPrescriptions(JavaPrescriptions prescriptions) {
    this.prescriptions = prescriptions;
  }

  @Override
  public Optional<LabPrescription> prescriptionOf(UUID prescription) {
    return prescriptions
      .prescription(prescription)
      .map(found -> new LabPrescription(prescription, new PatientId(found.patient()), new DoctorId(found.prescriber()), found.issuedOn(), found.labTests()));
  }
}
