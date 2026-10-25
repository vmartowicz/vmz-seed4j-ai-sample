package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.secondary.employee;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.Employee;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeEmail;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeName;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.Role;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.TeamName;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryEmployeesRepository implements EmployeesRepository {

  private static final String DEVELOPMENT = "Development";
  private static final String SALES = "Sales";

  private final List<Employee> employees = List.of(
    employee(1, "Marc", DEVELOPMENT, Role.MANAGER),
    employee(2, "Emma", DEVELOPMENT, Role.EMPLOYEE),
    employee(3, "Lucas", DEVELOPMENT, Role.EMPLOYEE),
    employee(4, "Lea", DEVELOPMENT, Role.EMPLOYEE),
    employee(5, "Hugo", DEVELOPMENT, Role.EMPLOYEE),
    employee(6, "Chloe", DEVELOPMENT, Role.EMPLOYEE),
    employee(7, "Sophie", SALES, Role.MANAGER),
    employee(8, "Paul", SALES, Role.EMPLOYEE),
    employee(9, "Ines", "Human resources", Role.HR)
  );

  private static Employee employee(int number, String name, String team, Role role) {
    return Employee.builder()
      .id(new EmployeeId(UUID.fromString("00000000-0000-4000-8000-%012d".formatted(number))))
      .name(new EmployeeName(name))
      .email(new EmployeeEmail(name.toLowerCase(Locale.ROOT) + "@company.com"))
      .team(new TeamName(team))
      .role(role)
      .build();
  }

  @Override
  public Optional<Employee> get(EmployeeId employee) {
    return employees
      .stream()
      .filter(candidate -> candidate.id().equals(employee))
      .findFirst();
  }

  @Override
  public List<Employee> all() {
    return employees;
  }

  @Override
  public List<Employee> membersOf(TeamName team) {
    return employees
      .stream()
      .filter(employee -> employee.team().equals(team))
      .toList();
  }
}
