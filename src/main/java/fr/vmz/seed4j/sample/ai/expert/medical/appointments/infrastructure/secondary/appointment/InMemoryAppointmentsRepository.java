package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.secondary.appointment;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Appointment;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentId;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentsRepository;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryAppointmentsRepository implements AppointmentsRepository {

  private final Map<AppointmentId, Appointment> appointments = new ConcurrentHashMap<>();

  @Override
  public void save(Appointment appointment) {
    appointments.put(appointment.id(), appointment);
  }

  @Override
  public Optional<Appointment> get(AppointmentId appointment) {
    return Optional.ofNullable(appointments.get(appointment));
  }

  @Override
  public List<Appointment> ofDoctor(DoctorId doctor) {
    return appointments
      .values()
      .stream()
      .filter(appointment -> appointment.doctor().equals(doctor))
      .toList();
  }

  @Override
  public List<Appointment> ofPatient(PatientId patient) {
    return appointments
      .values()
      .stream()
      .filter(appointment -> appointment.patient().equals(patient))
      .toList();
  }
}
