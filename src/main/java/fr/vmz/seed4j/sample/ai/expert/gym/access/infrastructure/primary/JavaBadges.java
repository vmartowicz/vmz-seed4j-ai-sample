package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.access.application.AccessApplicationService;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaBadges {

  private final AccessApplicationService access;

  public JavaBadges(AccessApplicationService access) {
    this.access = access;
  }

  public String issueFor(UUID member) {
    return access.issueBadge(new MemberId(member)).number().get();
  }
}
