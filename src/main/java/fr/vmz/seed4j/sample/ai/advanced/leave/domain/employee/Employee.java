package fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import org.jspecify.annotations.Nullable;

public final class Employee {

  private final EmployeeId id;
  private final EmployeeName name;
  private final EmployeeEmail email;
  private final TeamName team;
  private final Role role;

  private Employee(EmployeeBuilder builder) {
    Assert.notNull("id", builder.id);
    Assert.notNull("name", builder.name);
    Assert.notNull("email", builder.email);
    Assert.notNull("team", builder.team);
    Assert.notNull("role", builder.role);

    id = builder.id;
    name = builder.name;
    email = builder.email;
    team = builder.team;
    role = builder.role;
  }

  public static EmployeeBuilder builder() {
    return new EmployeeBuilder();
  }

  public boolean manages(Employee other) {
    return role == Role.MANAGER && team.equals(other.team()) && !id.equals(other.id());
  }

  public boolean isHumanResources() {
    return role == Role.HR;
  }

  public EmployeeId id() {
    return id;
  }

  public EmployeeName name() {
    return name;
  }

  public EmployeeEmail email() {
    return email;
  }

  public TeamName team() {
    return team;
  }

  public Role role() {
    return role;
  }

  public static final class EmployeeBuilder {

    private @Nullable EmployeeId id;
    private @Nullable EmployeeName name;
    private @Nullable EmployeeEmail email;
    private @Nullable TeamName team;
    private @Nullable Role role;

    private EmployeeBuilder() {}

    public EmployeeBuilder id(EmployeeId id) {
      this.id = id;

      return this;
    }

    public EmployeeBuilder name(EmployeeName name) {
      this.name = name;

      return this;
    }

    public EmployeeBuilder email(EmployeeEmail email) {
      this.email = email;

      return this;
    }

    public EmployeeBuilder team(TeamName team) {
      this.team = team;

      return this;
    }

    public EmployeeBuilder role(Role role) {
      this.role = role;

      return this;
    }

    public Employee build() {
      return new Employee(this);
    }
  }
}
