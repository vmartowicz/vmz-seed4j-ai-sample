package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.application.CallForPapersApplicationService;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaConfirmedTalks {

  private final CallForPapersApplicationService cfp;

  public JavaConfirmedTalks(CallForPapersApplicationService cfp) {
    this.cfp = cfp;
  }

  public Optional<JavaConfirmedTalk> talk(UUID proposal) {
    return cfp.confirmedTalk(new ProposalId(proposal)).map(JavaConfirmedTalk::from);
  }

  public boolean isConfirmedWorkshop(UUID proposal) {
    return cfp.isConfirmedWorkshop(proposal);
  }
}
