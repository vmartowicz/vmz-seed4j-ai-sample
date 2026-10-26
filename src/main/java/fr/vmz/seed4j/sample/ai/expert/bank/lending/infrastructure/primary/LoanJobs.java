package fr.vmz.seed4j.sample.ai.expert.bank.lending.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.lending.application.LendingApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class LoanJobs {

  private final LendingApplicationService lending;

  LoanJobs(LendingApplicationService lending) {
    this.lending = lending;
  }

  @Scheduled(cron = "0 0 6 * * *", zone = "Europe/Paris")
  void run() {
    lending.disburse();
    lending.collectInstalments();
  }
}
