package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.application.AppointmentsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.DoctorKind;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaDoctors {

  private final AppointmentsApplicationService appointments;

  public JavaDoctors(AppointmentsApplicationService appointments) {
    this.appointments = appointments;
  }

  public boolean exists(UUID doctor) {
    return appointments.findDoctor(new DoctorId(doctor)).isPresent();
  }

  public Optional<UUID> onCallReplacing(UUID doctor) {
    return appointments.onCallReplacing(new DoctorId(doctor)).map(DoctorId::get);
  }

  public Optional<JavaTariff> tariffOf(UUID doctor) {
    return appointments.findDoctor(new DoctorId(doctor)).map(found -> new JavaTariff(found.kind() == DoctorKind.SPECIALIST, found.feeOverrun()));
  }
}
