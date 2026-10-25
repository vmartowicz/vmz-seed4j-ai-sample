package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.Year;

public record BalanceSummary(Year year, LeaveDays entitlement, LeaveDays approved, LeaveDays pending) {
  public BalanceSummary {
    Assert.notNull("year", year);
    Assert.notNull("entitlement", entitlement);
    Assert.notNull("approved", approved);
    Assert.notNull("pending", pending);
  }

  public LeaveDays consumed() {
    return approved.plus(pending);
  }

  public LeaveDays remaining() {
    return entitlement.minus(consumed());
  }
}
