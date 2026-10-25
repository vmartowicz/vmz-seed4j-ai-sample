package fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee;

import java.util.Locale;

public final class EmployeesFixture {

  public static final Employee MARC = employee("Marc", "Development", Role.MANAGER);
  public static final Employee EMMA = employee("Emma", "Development", Role.EMPLOYEE);
  public static final Employee LUCAS = employee("Lucas", "Development", Role.EMPLOYEE);
  public static final Employee LEA = employee("Lea", "Development", Role.EMPLOYEE);
  public static final Employee HUGO = employee("Hugo", "Development", Role.EMPLOYEE);
  public static final Employee SOPHIE = employee("Sophie", "Sales", Role.MANAGER);
  public static final Employee INES = employee("Ines", "Human resources", Role.HR);

  private EmployeesFixture() {}

  public static Employee employee(String name, String team, Role role) {
    return Employee.builder()
      .id(EmployeeId.newId())
      .name(new EmployeeName(name))
      .email(new EmployeeEmail(name.toLowerCase(Locale.ROOT) + "@company.com"))
      .team(new TeamName(team))
      .role(role)
      .build();
  }
}
