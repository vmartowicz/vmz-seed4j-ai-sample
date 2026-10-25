package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public record LeaveRequestToCreate(EmployeeId employee, LeavePeriod period, LocalDate requestedOn) {
  public LeaveRequestToCreate {
    Assert.notNull("employee", employee);
    Assert.notNull("period", period);
    Assert.notNull("requestedOn", requestedOn);
  }

  public LeaveRequest create() {
    return new LeaveRequest(LeaveRequestId.newId(), this);
  }
}
