package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.application.LeaveRequestsApplicationService;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestId;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave/requests")
class LeaveRequestsResource {

  private static final String EMPLOYEE_HEADER = "X-Employee-Id";

  private final LeaveRequestsApplicationService requests;

  LeaveRequestsResource(LeaveRequestsApplicationService requests) {
    this.requests = requests;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestLeaveRequest request(@RequestHeader(EMPLOYEE_HEADER) UUID employee, @RequestBody RestLeavePeriod period) {
    return RestLeaveRequest.from(requests.request(new EmployeeId(employee), period.toDomain()));
  }

  @GetMapping("/mine")
  List<RestLeaveRequest> mine(@RequestHeader(EMPLOYEE_HEADER) UUID employee) {
    return toRest(requests.requestsOf(new EmployeeId(employee)));
  }

  @GetMapping("/to-review")
  List<RestLeaveRequest> toReview(@RequestHeader(EMPLOYEE_HEADER) UUID manager) {
    return toRest(requests.toReview(new EmployeeId(manager)));
  }

  @PostMapping("/{requestId}/approval")
  RestLeaveRequest approve(@RequestHeader(EMPLOYEE_HEADER) UUID manager, @PathVariable UUID requestId) {
    return RestLeaveRequest.from(requests.approve(new EmployeeId(manager), new LeaveRequestId(requestId)));
  }

  @PostMapping("/{requestId}/rejection")
  RestLeaveRequest reject(
    @RequestHeader(EMPLOYEE_HEADER) UUID manager,
    @PathVariable UUID requestId,
    @RequestBody RestRejection rejection
  ) {
    return RestLeaveRequest.from(requests.reject(new EmployeeId(manager), new LeaveRequestId(requestId), rejection.toDomain()));
  }

  @PostMapping("/{requestId}/cancellation")
  RestLeaveRequest cancel(@RequestHeader(EMPLOYEE_HEADER) UUID employee, @PathVariable UUID requestId) {
    return RestLeaveRequest.from(requests.cancel(new EmployeeId(employee), new LeaveRequestId(requestId)));
  }

  private static List<RestLeaveRequest> toRest(List<LeaveRequest> requests) {
    return requests.stream().map(RestLeaveRequest::from).toList();
  }
}
