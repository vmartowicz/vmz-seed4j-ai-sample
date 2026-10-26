package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record CareSheetId(UUID id) {
  public CareSheetId {
    Assert.notNull("careSheetId", id);
  }

  public static CareSheetId newId() {
    return new CareSheetId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
