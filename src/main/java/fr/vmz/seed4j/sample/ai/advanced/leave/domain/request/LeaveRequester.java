package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveAccessDeniedException;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalancesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesRepository;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class LeaveRequester {

  private static final Comparator<LeaveRequest> LATEST_FIRST = Comparator.comparing(
    (LeaveRequest request) -> request.period().start()
  ).reversed();

  private final EmployeesRepository employees;
  private final LeaveRequestsRepository requests;
  private final BalanceCalculator calculator;

  public LeaveRequester(EmployeesRepository employees, LeaveBalancesRepository balances, LeaveRequestsRepository requests) {
    this.employees = employees;
    this.requests = requests;
    calculator = new BalanceCalculator(balances, requests);
  }

  public LeaveRequest request(EmployeeId employeeId, LeavePeriod period, LocalDate today) {
    Assert.notNull("period", period);
    Assert.notNull("today", today);

    var employee = employees.employee(employeeId);
    if (period.start().date().isBefore(today)) {
      throw new LeaveInThePastException(period);
    }

    var overlapping = requests
      .ofEmployee(employee.id())
      .stream()
      .filter(LeaveRequest::isActive)
      .anyMatch(other -> other.period().overlaps(period));
    if (overlapping) {
      throw new OverlappingLeaveException(period);
    }

    var remaining = calculator.summaryOf(employee.id(), period.year()).remaining();
    if (!remaining.covers(period.days())) {
      throw new InsufficientLeaveBalanceException(remaining, period.days());
    }

    var request = new LeaveRequestToCreate(employee.id(), period, today).create();
    requests.save(request);

    return request;
  }

  public LeaveRequest cancel(EmployeeId by, LeaveRequestId requestId) {
    var request = requests.request(requestId);
    if (!request.employee().equals(by)) {
      throw new LeaveAccessDeniedException("Only the employee who asked for a leave can cancel it");
    }

    request.cancel();
    requests.save(request);

    return request;
  }

  public List<LeaveRequest> requestsOf(EmployeeId employee) {
    return requests.ofEmployee(employee).stream().sorted(LATEST_FIRST).toList();
  }
}
