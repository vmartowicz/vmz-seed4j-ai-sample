package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.application.LeaveEmployeesApplicationService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave/employees")
class LeaveEmployeesResource {

  private final LeaveEmployeesApplicationService employees;

  LeaveEmployeesResource(LeaveEmployeesApplicationService employees) {
    this.employees = employees;
  }

  @GetMapping
  List<RestEmployee> list() {
    return employees.all().stream().map(RestEmployee::from).toList();
  }
}
