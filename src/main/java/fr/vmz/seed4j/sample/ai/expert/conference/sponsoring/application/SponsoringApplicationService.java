package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.application;

import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorDesk;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorId;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorTickets;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.Sponsorship;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorshipToSign;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorshipsRepository;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class SponsoringApplicationService {

  private final SponsorDesk desk;
  private final Clock clock;

  public SponsoringApplicationService(SponsorshipsRepository sponsorships, SponsorTickets tickets, Clock clock) {
    desk = new SponsorDesk(sponsorships, tickets);
    this.clock = clock;
  }

  public Sponsorship sign(SponsorshipToSign sponsorship) {
    return desk.sign(sponsorship, today());
  }

  public Sponsorship sponsorship(SponsorId sponsorship) {
    return desk.sponsorship(sponsorship);
  }

  public Sponsorship pay(SponsorId sponsorship) {
    return desk.pay(sponsorship, today());
  }

  public void releaseUnpaid() {
    desk.releaseUnpaid(today());
  }

  public boolean claimTalk(SponsorId sponsorship) {
    return desk.claimTalk(sponsorship);
  }

  private LocalDate today() {
    return LocalDate.now(clock);
  }
}
