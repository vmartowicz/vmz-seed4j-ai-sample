package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record MemberId(UUID id) {
  public MemberId {
    Assert.notNull("id", id);
  }

  public static MemberId newId() {
    return new MemberId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
