package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.billing.application.MedicalBillingApplicationService;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.ActToBill;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaMedicalBilling {

  private final MedicalBillingApplicationService billing;

  public JavaMedicalBilling(MedicalBillingApplicationService billing) {
    this.billing = billing;
  }

  public void bill(UUID consultation, UUID patient, UUID doctor, LocalDate date) {
    billing.bill(new ActToBill(consultation, new PatientId(patient), new DoctorId(doctor), date));
  }
}
