package fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record EmployeeId(UUID id) {
  public EmployeeId {
    Assert.notNull("id", id);
  }

  public static EmployeeId newId() {
    return new EmployeeId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
