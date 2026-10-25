package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record LeaveRequestId(UUID id) {
  public LeaveRequestId {
    Assert.notNull("id", id);
  }

  public static LeaveRequestId newId() {
    return new LeaveRequestId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
