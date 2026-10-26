package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.application.RentalBillingApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class DepositReleaseJob {

  private final RentalBillingApplicationService billing;

  DepositReleaseJob(RentalBillingApplicationService billing) {
    this.billing = billing;
  }

  @Scheduled(cron = "0 30 6 * * *", zone = "Europe/Paris")
  void release() {
    billing.releaseDeposits();
  }
}
