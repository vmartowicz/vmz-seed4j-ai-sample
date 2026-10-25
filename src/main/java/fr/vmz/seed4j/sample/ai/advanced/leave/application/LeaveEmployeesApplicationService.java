package fr.vmz.seed4j.sample.ai.advanced.leave.application;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.Employee;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeaveEmployeesApplicationService {

  private final EmployeesRepository employees;

  public LeaveEmployeesApplicationService(EmployeesRepository employees) {
    this.employees = employees;
  }

  public List<Employee> all() {
    return employees.all();
  }
}
