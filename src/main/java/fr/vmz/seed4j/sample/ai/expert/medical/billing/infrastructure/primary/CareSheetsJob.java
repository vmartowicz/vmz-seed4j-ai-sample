package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.billing.application.MedicalBillingApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class CareSheetsJob {

  private final MedicalBillingApplicationService billing;

  CareSheetsJob(MedicalBillingApplicationService billing) {
    this.billing = billing;
  }

  @Scheduled(cron = "0 0 22 * * *", zone = "Europe/Paris")
  void send() {
    billing.sendPendingCareSheets();
  }
}
