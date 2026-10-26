package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.util.List;
import java.util.Optional;

public interface DoctorsRepository {
  void save(Doctor doctor);

  Optional<Doctor> get(DoctorId doctor);

  List<Doctor> all();

  default Doctor doctorOf(DoctorId doctor) {
    return get(doctor).orElseThrow(() -> new UnknownDoctorException(doctor));
  }
}
