package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalance;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalancesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import java.time.Year;
import java.util.List;

public class BalanceCalculator {

  private final LeaveBalancesRepository balances;
  private final LeaveRequestsRepository requests;

  public BalanceCalculator(LeaveBalancesRepository balances, LeaveRequestsRepository requests) {
    this.balances = balances;
    this.requests = requests;
  }

  public LeaveBalance balanceOf(EmployeeId employee, Year year) {
    return balances.get(employee, year).orElseGet(() -> LeaveBalance.standard(employee, year));
  }

  public BalanceSummary summaryOf(EmployeeId employee, Year year) {
    return summaryOf(balanceOf(employee, year));
  }

  public BalanceSummary summaryOf(LeaveBalance balance) {
    var requestsOfTheYear = requests
      .ofEmployee(balance.employee())
      .stream()
      .filter(request -> request.period().year().equals(balance.year()))
      .toList();

    return new BalanceSummary(
      balance.year(),
      balance.entitlement(),
      total(requestsOfTheYear, LeaveRequestStatus.APPROVED),
      total(requestsOfTheYear, LeaveRequestStatus.PENDING)
    );
  }

  private static LeaveDays total(List<LeaveRequest> requests, LeaveRequestStatus status) {
    return requests
      .stream()
      .filter(request -> request.status() == status)
      .map(request -> request.period().days())
      .reduce(LeaveDays.ZERO, LeaveDays::plus);
  }
}
