package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.Employee;
import java.util.UUID;

record RestEmployee(UUID id, String name, String email, String team, String role) {
  static RestEmployee from(Employee employee) {
    return new RestEmployee(
      employee.id().get(),
      employee.name().get(),
      employee.email().get(),
      employee.team().get(),
      employee.role().name()
    );
  }
}
