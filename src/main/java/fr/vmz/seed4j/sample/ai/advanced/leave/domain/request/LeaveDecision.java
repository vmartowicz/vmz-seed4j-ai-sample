package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeEmail;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Optional;

public record LeaveDecision(EmployeeEmail to, LeaveRequest request, Optional<RejectionComment> comment) {
  public LeaveDecision {
    Assert.notNull("to", to);
    Assert.notNull("request", request);
    Assert.notNull("comment", comment);
  }
}
