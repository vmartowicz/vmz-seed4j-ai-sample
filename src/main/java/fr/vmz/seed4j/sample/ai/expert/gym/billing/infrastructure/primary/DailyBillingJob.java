package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.application.BillingApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class DailyBillingJob {

  private final BillingApplicationService billing;

  DailyBillingJob(BillingApplicationService billing) {
    this.billing = billing;
  }

  @Scheduled(cron = "0 0 6 * * *", zone = "Europe/Paris")
  void run() {
    billing.runDaily();
  }
}
