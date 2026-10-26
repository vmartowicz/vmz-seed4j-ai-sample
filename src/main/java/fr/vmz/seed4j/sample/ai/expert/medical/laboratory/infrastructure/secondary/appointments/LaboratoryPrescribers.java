package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.secondary.appointments;

import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.Prescribers;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary.JavaDoctors;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class LaboratoryPrescribers implements Prescribers {

  private final JavaDoctors doctors;

  LaboratoryPrescribers(JavaDoctors doctors) {
    this.doctors = doctors;
  }

  @Override
  public Optional<DoctorId> onCallReplacing(DoctorId prescriber) {
    return doctors.onCallReplacing(prescriber.get()).map(DoctorId::new);
  }
}
