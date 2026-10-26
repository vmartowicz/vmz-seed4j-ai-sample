package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.secondary.billing;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.Consultation;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ConsultationBilling;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.primary.JavaMedicalBilling;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
class RecordsConsultationBilling implements ConsultationBilling {

  private final JavaMedicalBilling billing;

  RecordsConsultationBilling(JavaMedicalBilling billing) {
    this.billing = billing;
  }

  @Override
  public void closed(Consultation consultation) {
    billing.bill(
      consultation.id().get(),
      consultation.patient().get(),
      consultation.doctor().get(),
      consultation.closedAt().map(LocalDateTime::toLocalDate).orElseThrow()
    );
  }
}
