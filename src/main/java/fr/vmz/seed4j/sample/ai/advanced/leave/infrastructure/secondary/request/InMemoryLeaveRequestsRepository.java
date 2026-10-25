package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.secondary.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryLeaveRequestsRepository implements LeaveRequestsRepository {

  private final Map<LeaveRequestId, LeaveRequest> requests = new ConcurrentHashMap<>();

  @Override
  public void save(LeaveRequest request) {
    requests.put(request.id(), request);
  }

  @Override
  public Optional<LeaveRequest> get(LeaveRequestId request) {
    return Optional.ofNullable(requests.get(request));
  }

  @Override
  public List<LeaveRequest> ofEmployee(EmployeeId employee) {
    return ofEmployees(List.of(employee));
  }

  @Override
  public List<LeaveRequest> ofEmployees(Collection<EmployeeId> employees) {
    return requests
      .values()
      .stream()
      .filter(request -> employees.contains(request.employee()))
      .toList();
  }
}
