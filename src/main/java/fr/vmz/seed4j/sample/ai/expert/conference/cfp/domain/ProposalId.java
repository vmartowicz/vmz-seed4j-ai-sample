package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record ProposalId(UUID id) {
  public ProposalId {
    Assert.notNull("proposalId", id);
  }

  public static ProposalId newId() {
    return new ProposalId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
