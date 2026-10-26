package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.secondary.patient;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Patient;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.PatientsRepository;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryPatientsRepository implements PatientsRepository {

  private final Map<PatientId, Patient> patients = new ConcurrentHashMap<>();

  @Override
  public void save(Patient patient) {
    patients.put(patient.id(), patient);
  }

  @Override
  public Optional<Patient> get(PatientId patient) {
    return Optional.ofNullable(patients.get(patient));
  }
}
