package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.secondary.sponsoring;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.SponsoredTalks;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.primary.JavaSponsorships;
import java.util.UUID;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class CallForPapersSponsors implements SponsoredTalks {

  private final JavaSponsorships sponsorships;

  CallForPapersSponsors(@Lazy JavaSponsorships sponsorships) {
    this.sponsorships = sponsorships;
  }

  @Override
  public boolean claim(UUID sponsor) {
    return sponsorships.claimSponsoredTalk(sponsor);
  }
}
