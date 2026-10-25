package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestsRepository {
  void save(LeaveRequest request);

  Optional<LeaveRequest> get(LeaveRequestId request);

  List<LeaveRequest> ofEmployee(EmployeeId employee);

  List<LeaveRequest> ofEmployees(Collection<EmployeeId> employees);

  default LeaveRequest request(LeaveRequestId request) {
    return get(request).orElseThrow(() -> new UnknownLeaveRequestException(request));
  }
}
