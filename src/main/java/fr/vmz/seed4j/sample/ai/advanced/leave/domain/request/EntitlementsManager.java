package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveAccessDeniedException;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalancesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesRepository;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.Year;

public class EntitlementsManager {

  private final EmployeesRepository employees;
  private final LeaveBalancesRepository balances;
  private final BalanceCalculator calculator;

  public EntitlementsManager(EmployeesRepository employees, LeaveBalancesRepository balances, LeaveRequestsRepository requests) {
    this.employees = employees;
    this.balances = balances;
    calculator = new BalanceCalculator(balances, requests);
  }

  public BalanceSummary changeEntitlement(EmployeeId humanResourcesId, EmployeeId employeeId, Year year, LeaveDays entitlement) {
    Assert.notNull("year", year);
    Assert.notNull("entitlement", entitlement);

    if (!employees.employee(humanResourcesId).isHumanResources()) {
      throw new LeaveAccessDeniedException("Only human resources can change leave balances");
    }

    var balance = calculator.balanceOf(employees.employee(employeeId).id(), year);
    var consumed = calculator.summaryOf(balance).consumed();
    if (!entitlement.covers(consumed)) {
      throw new EntitlementBelowConsumedDaysException(consumed);
    }

    balance.changeEntitlement(entitlement);
    balances.save(balance);

    return calculator.summaryOf(balance);
  }

  public BalanceSummary summaryOf(EmployeeId employee, Year year) {
    return calculator.summaryOf(employees.employee(employee).id(), year);
  }
}
