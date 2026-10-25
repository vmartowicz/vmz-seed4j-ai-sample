package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record SponsorId(UUID id) {
  public SponsorId {
    Assert.notNull("sponsorId", id);
  }

  public static SponsorId newId() {
    return new SponsorId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
