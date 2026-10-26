package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.secondary.records;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingPrescription;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingPrescriptions;
import fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary.JavaPrescriptions;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class ImagingRecordsPrescriptions implements ImagingPrescriptions {

  private final JavaPrescriptions prescriptions;

  ImagingRecordsPrescriptions(JavaPrescriptions prescriptions) {
    this.prescriptions = prescriptions;
  }

  @Override
  public Optional<ImagingPrescription> prescriptionOf(UUID prescription) {
    return prescriptions
      .prescription(prescription)
      .map(found ->
        new ImagingPrescription(prescription, new PatientId(found.patient()), new DoctorId(found.prescriber()), found.issuedOn(), found.imagingExams())
      );
  }
}
