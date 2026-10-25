package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

public record Badge(BadgeNumber number, MemberId member) {
  public Badge {
    Assert.notNull("number", number);
    Assert.notNull("member", member);
  }
}
