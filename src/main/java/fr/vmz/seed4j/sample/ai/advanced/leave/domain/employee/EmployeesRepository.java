package fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee;

import java.util.List;
import java.util.Optional;

public interface EmployeesRepository {
  Optional<Employee> get(EmployeeId employee);

  List<Employee> all();

  List<Employee> membersOf(TeamName team);

  default Employee employee(EmployeeId employee) {
    return get(employee).orElseThrow(() -> new UnknownEmployeeException(employee));
  }
}
