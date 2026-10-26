package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.application.RetailAccountsApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class AccountClosingJobs {

  private final RetailAccountsApplicationService accounts;

  AccountClosingJobs(RetailAccountsApplicationService accounts) {
    this.accounts = accounts;
  }

  @Scheduled(cron = "0 55 23 * * *", zone = "Europe/Paris")
  void accrueInterest() {
    accounts.accrueInterest();
  }

  @Scheduled(cron = "0 0 2 1 * *", zone = "Europe/Paris")
  void closeMonth() {
    accounts.closeMonth();
  }
}
