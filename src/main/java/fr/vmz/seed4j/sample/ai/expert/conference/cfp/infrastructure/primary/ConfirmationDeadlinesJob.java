package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.application.CallForPapersApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class ConfirmationDeadlinesJob {

  private final CallForPapersApplicationService cfp;

  ConfirmationDeadlinesJob(CallForPapersApplicationService cfp) {
    this.cfp = cfp;
  }

  @Scheduled(cron = "0 0 2 * * *", zone = "Europe/Paris")
  void expire() {
    cfp.expireUnconfirmed();
  }
}
