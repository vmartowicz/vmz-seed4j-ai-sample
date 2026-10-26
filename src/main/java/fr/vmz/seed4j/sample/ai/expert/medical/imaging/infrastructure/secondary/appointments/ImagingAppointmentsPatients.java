package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.secondary.appointments;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingPatient;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingPatients;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary.JavaPatients;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class ImagingAppointmentsPatients implements ImagingPatients {

  private final JavaPatients patients;

  ImagingAppointmentsPatients(JavaPatients patients) {
    this.patients = patients;
  }

  @Override
  public Optional<ImagingPatient> profileOf(PatientId patient) {
    return patients.profileOf(patient.get()).map(found -> new ImagingPatient(found.female(), found.birthDate()));
  }
}
