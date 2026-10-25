package fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee;

import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;

@UnitTest
class EmployeeTest {

  @Test
  void shouldBuildEmployee() {
    assertThat(EMMA.id()).isNotNull();
    assertThat(EMMA.name()).isEqualTo(new EmployeeName("Emma"));
    assertThat(EMMA.email()).isEqualTo(new EmployeeEmail("emma@company.com"));
    assertThat(EMMA.team()).isEqualTo(new TeamName("Development"));
    assertThat(EMMA.role()).isEqualTo(Role.EMPLOYEE);
  }

  @Test
  void shouldNotBuildEmployeeWithoutRole() {
    var builder = Employee.builder()
      .id(EmployeeId.newId())
      .name(new EmployeeName("Emma"))
      .email(new EmployeeEmail("emma@company.com"))
      .team(new TeamName("Development"));

    assertThatThrownBy(builder::build).isExactlyInstanceOf(MissingMandatoryValueException.class).hasMessageContaining("role");
  }

  @Test
  void shouldManageEmployeesOfItsTeam() {
    assertThat(MARC.manages(EMMA)).isTrue();
  }

  @Test
  void shouldNotManageEmployeesOfAnotherTeam() {
    assertThat(SOPHIE.manages(EMMA)).isFalse();
  }

  @Test
  void shouldNotManageItself() {
    assertThat(MARC.manages(MARC)).isFalse();
  }

  @Test
  void shouldNotManageWithoutManagerRole() {
    assertThat(LUCAS.manages(EMMA)).isFalse();
  }

  @Test
  void shouldKnowHumanResources() {
    assertThat(INES.isHumanResources()).isTrue();
    assertThat(MARC.isHumanResources()).isFalse();
  }
}
