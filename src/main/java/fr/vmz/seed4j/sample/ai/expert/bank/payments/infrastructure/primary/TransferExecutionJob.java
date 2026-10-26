package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.application.PaymentsApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class TransferExecutionJob {

  private final PaymentsApplicationService payments;

  TransferExecutionJob(PaymentsApplicationService payments) {
    this.payments = payments;
  }

  @Scheduled(cron = "0 0 7 * * MON-FRI", zone = "Europe/Paris")
  void execute() {
    payments.executeDueTransfers();
  }
}
