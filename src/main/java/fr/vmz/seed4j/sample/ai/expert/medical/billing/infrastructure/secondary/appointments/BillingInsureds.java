package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.secondary.appointments;

import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.Insurance;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.Insureds;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary.JavaPatients;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class BillingInsureds implements Insureds {

  private final JavaPatients patients;

  BillingInsureds(JavaPatients patients) {
    this.patients = patients;
  }

  @Override
  public Optional<Insurance> insuranceOf(PatientId patient) {
    return patients
      .insuranceOf(patient.get())
      .map(found ->
        new Insurance(new PatientId(found.insured()), found.referringDoctorDeclared(), found.longTermIllness(), found.solidarityHealthCover())
      );
  }
}
