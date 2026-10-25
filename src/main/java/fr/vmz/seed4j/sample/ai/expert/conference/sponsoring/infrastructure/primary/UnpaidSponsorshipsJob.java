package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.application.SponsoringApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class UnpaidSponsorshipsJob {

  private final SponsoringApplicationService sponsoring;

  UnpaidSponsorshipsJob(SponsoringApplicationService sponsoring) {
    this.sponsoring = sponsoring;
  }

  @Scheduled(cron = "0 0 1 * * *", zone = "Europe/Paris")
  void release() {
    sponsoring.releaseUnpaid();
  }
}
