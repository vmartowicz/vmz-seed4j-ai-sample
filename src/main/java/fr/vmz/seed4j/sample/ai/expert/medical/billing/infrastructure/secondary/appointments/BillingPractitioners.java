package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.secondary.appointments;

import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.Practitioners;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.Tariff;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary.JavaDoctors;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class BillingPractitioners implements Practitioners {

  private final JavaDoctors doctors;

  BillingPractitioners(JavaDoctors doctors) {
    this.doctors = doctors;
  }

  @Override
  public Optional<Tariff> tariffOf(DoctorId doctor) {
    return doctors.tariffOf(doctor.get()).map(found -> new Tariff(found.specialist(), found.feeOverrun()));
  }
}
