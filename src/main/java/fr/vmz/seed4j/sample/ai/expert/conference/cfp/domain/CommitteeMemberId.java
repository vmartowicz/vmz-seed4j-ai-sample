package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record CommitteeMemberId(UUID id) {
  public CommitteeMemberId {
    Assert.notNull("memberId", id);
  }

  public static CommitteeMemberId newId() {
    return new CommitteeMemberId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
