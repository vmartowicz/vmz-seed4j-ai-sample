package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.secondary.doctor;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Doctor;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.DoctorsRepository;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryDoctorsRepository implements DoctorsRepository {

  private final Map<DoctorId, Doctor> doctors = new ConcurrentHashMap<>();

  @Override
  public void save(Doctor doctor) {
    doctors.put(doctor.id(), doctor);
  }

  @Override
  public Optional<Doctor> get(DoctorId doctor) {
    return Optional.ofNullable(doctors.get(doctor));
  }

  @Override
  public List<Doctor> all() {
    return List.copyOf(doctors.values());
  }
}
