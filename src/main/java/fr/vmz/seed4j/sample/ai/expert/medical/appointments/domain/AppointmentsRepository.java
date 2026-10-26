package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.List;
import java.util.Optional;

public interface AppointmentsRepository {
  void save(Appointment appointment);

  Optional<Appointment> get(AppointmentId appointment);

  List<Appointment> ofDoctor(DoctorId doctor);

  List<Appointment> ofPatient(PatientId patient);

  default Appointment appointmentOf(AppointmentId appointment) {
    return get(appointment).orElseThrow(() -> new UnknownAppointmentException(appointment));
  }
}
