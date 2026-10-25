package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.application.SponsoringApplicationService;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorId;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaSponsorships {

  private final SponsoringApplicationService sponsoring;

  public JavaSponsorships(SponsoringApplicationService sponsoring) {
    this.sponsoring = sponsoring;
  }

  public boolean claimSponsoredTalk(UUID sponsorship) {
    return sponsoring.claimTalk(new SponsorId(sponsorship));
  }
}
