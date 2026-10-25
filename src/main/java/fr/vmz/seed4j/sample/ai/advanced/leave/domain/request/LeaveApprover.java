package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveAccessDeniedException;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.Employee;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.Role;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LeaveApprover {

  private static final long MAX_SIMULTANEOUS_LEAVES = 3;
  private static final Comparator<LeaveRequest> SOONEST_FIRST = Comparator.comparing(request -> request.period().start());

  private final EmployeesRepository employees;
  private final LeaveRequestsRepository requests;
  private final LeaveNotifier notifier;

  public LeaveApprover(EmployeesRepository employees, LeaveRequestsRepository requests, LeaveNotifier notifier) {
    this.employees = employees;
    this.requests = requests;
    this.notifier = notifier;
  }

  public LeaveRequest approve(EmployeeId managerId, LeaveRequestId requestId) {
    var request = requests.request(requestId);
    var employee = managedEmployee(managerId, request);
    request.assertPending();
    assertTeamLimit(employee, request);

    request.approve();
    requests.save(request);
    notifier.decisionMade(new LeaveDecision(employee.email(), request, Optional.empty()));

    return request;
  }

  public LeaveRequest reject(EmployeeId managerId, LeaveRequestId requestId, Optional<RejectionComment> comment) {
    var request = requests.request(requestId);
    var employee = managedEmployee(managerId, request);

    request.reject(comment);
    requests.save(request);
    notifier.decisionMade(new LeaveDecision(employee.email(), request, comment));

    return request;
  }

  public List<LeaveRequest> toReview(EmployeeId managerId) {
    var manager = employees.employee(managerId);
    if (manager.role() != Role.MANAGER) {
      throw new LeaveAccessDeniedException("Only managers review leave requests");
    }

    return requests
      .ofEmployees(teamOf(manager))
      .stream()
      .filter(request -> request.status() == LeaveRequestStatus.PENDING && !request.employee().equals(manager.id()))
      .sorted(SOONEST_FIRST)
      .toList();
  }

  private Employee managedEmployee(EmployeeId managerId, LeaveRequest request) {
    var manager = employees.employee(managerId);
    var employee = employees.employee(request.employee());
    if (!manager.manages(employee)) {
      throw new LeaveAccessDeniedException(manager.name().get() + " can't decide on the leaves of " + employee.name().get());
    }

    return employee;
  }

  private void assertTeamLimit(Employee employee, LeaveRequest request) {
    var approvedLeaves = requests
      .ofEmployees(teamOf(employee))
      .stream()
      .filter(other -> other.status() == LeaveRequestStatus.APPROVED)
      .toList();

    request
      .period()
      .halfDays()
      .stream()
      .filter(halfDay -> approvedLeaves.stream().filter(other -> other.period().contains(halfDay)).count() >= MAX_SIMULTANEOUS_LEAVES)
      .findFirst()
      .ifPresent(halfDay -> {
        throw new TeamLimitReachedException(employee.team(), halfDay);
      });
  }

  private List<EmployeeId> teamOf(Employee employee) {
    return employees.membersOf(employee.team()).stream().map(Employee::id).toList();
  }
}
