package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.application.AppointmentsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Sex;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaPatients {

  private final AppointmentsApplicationService appointments;

  public JavaPatients(AppointmentsApplicationService appointments) {
    this.appointments = appointments;
  }

  public boolean exists(UUID patient) {
    return appointments.findPatient(new PatientId(patient)).isPresent();
  }

  public Optional<JavaPatientProfile> profileOf(UUID patient) {
    return appointments.findPatient(new PatientId(patient)).map(found -> new JavaPatientProfile(found.sex() == Sex.FEMALE, found.birthDate()));
  }

  public Optional<JavaInsurance> insuranceOf(UUID patient) {
    return appointments
      .findPatient(new PatientId(patient))
      .map(found ->
        new JavaInsurance(found.insured().get(), found.referringDoctor().isPresent(), found.longTermIllness(), found.solidarityHealthCover())
      );
  }
}
