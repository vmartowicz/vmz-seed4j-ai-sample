package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Optional;

public interface PatientsRepository {
  void save(Patient patient);

  Optional<Patient> get(PatientId patient);

  default Patient patientOf(PatientId patient) {
    return get(patient).orElseThrow(() -> new UnknownPatientException(patient));
  }
}
