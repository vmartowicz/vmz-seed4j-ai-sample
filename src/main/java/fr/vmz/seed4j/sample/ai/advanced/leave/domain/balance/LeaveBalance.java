package fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.Year;

public final class LeaveBalance {

  private final EmployeeId employee;
  private final Year year;
  private LeaveDays entitlement;

  private LeaveBalance(EmployeeId employee, Year year, LeaveDays entitlement) {
    Assert.notNull("employee", employee);
    Assert.notNull("year", year);

    this.employee = employee;
    this.year = year;
    this.entitlement = entitlement;
  }

  public static LeaveBalance standard(EmployeeId employee, Year year) {
    return new LeaveBalance(employee, year, LeaveDays.YEARLY_ENTITLEMENT);
  }

  public void changeEntitlement(LeaveDays newEntitlement) {
    Assert.notNull("newEntitlement", newEntitlement);

    entitlement = newEntitlement;
  }

  public EmployeeId employee() {
    return employee;
  }

  public Year year() {
    return year;
  }

  public LeaveDays entitlement() {
    return entitlement;
  }
}
