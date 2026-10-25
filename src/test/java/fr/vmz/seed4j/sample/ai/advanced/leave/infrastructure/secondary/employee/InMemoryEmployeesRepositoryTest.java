package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.secondary.employee;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.Role;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.TeamName;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryEmployeesRepositoryTest {

  private final InMemoryEmployeesRepository employees = new InMemoryEmployeesRepository();

  @Test
  void shouldKnowTheCompanyEmployees() {
    assertThat(employees.all())
      .extracting(employee -> employee.name().get())
      .containsExactly("Marc", "Emma", "Lucas", "Lea", "Hugo", "Chloe", "Sophie", "Paul", "Ines");
  }

  @Test
  void shouldGetEmployeeById() {
    var marc = employees.get(new EmployeeId(UUID.fromString("00000000-0000-4000-8000-000000000001"))).orElseThrow();

    assertThat(marc.name().get()).isEqualTo("Marc");
    assertThat(marc.email().get()).isEqualTo("marc@company.com");
    assertThat(marc.role()).isEqualTo(Role.MANAGER);
    assertThat(employees.get(EmployeeId.newId())).isEmpty();
  }

  @Test
  void shouldListMembersOfATeam() {
    assertThat(employees.membersOf(new TeamName("Sales"))).extracting(employee -> employee.name().get()).containsExactly("Sophie", "Paul");
  }
}
