package fr.vmz.seed4j.sample.ai.advanced.leave.application;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalancesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveApprover;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveNotifier;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeavePeriod;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequester;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.RejectionComment;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LeaveRequestsApplicationService {

  private final LeaveRequester requester;
  private final LeaveApprover approver;
  private final Clock clock;

  public LeaveRequestsApplicationService(
    EmployeesRepository employees,
    LeaveBalancesRepository balances,
    LeaveRequestsRepository requests,
    LeaveNotifier notifier,
    Clock clock
  ) {
    requester = new LeaveRequester(employees, balances, requests);
    approver = new LeaveApprover(employees, requests, notifier);
    this.clock = clock;
  }

  public LeaveRequest request(EmployeeId employee, LeavePeriod period) {
    return requester.request(employee, period, LocalDate.now(clock));
  }

  public List<LeaveRequest> requestsOf(EmployeeId employee) {
    return requester.requestsOf(employee);
  }

  public LeaveRequest cancel(EmployeeId employee, LeaveRequestId request) {
    return requester.cancel(employee, request);
  }

  public List<LeaveRequest> toReview(EmployeeId manager) {
    return approver.toReview(manager);
  }

  public LeaveRequest approve(EmployeeId manager, LeaveRequestId request) {
    return approver.approve(manager, request);
  }

  public LeaveRequest reject(EmployeeId manager, LeaveRequestId request, Optional<RejectionComment> comment) {
    return approver.reject(manager, request, comment);
  }
}
