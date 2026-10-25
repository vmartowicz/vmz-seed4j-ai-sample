package fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class EmployeeValuesTest {

  @Test
  void shouldNotBuildNameWithMoreThan100Characters() {
    assertThatThrownBy(() -> new EmployeeName("a".repeat(101))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldNotBuildBlankEmail() {
    assertThatThrownBy(() -> new EmployeeEmail(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildTeamWithMoreThan50Characters() {
    assertThatThrownBy(() -> new TeamName("a".repeat(51))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldExposeValues() {
    assertThat(new EmployeeName("Emma").get()).isEqualTo("Emma");
    assertThat(new EmployeeEmail("emma@company.com").get()).isEqualTo("emma@company.com");
    assertThat(new TeamName("Development").get()).isEqualTo("Development");
  }
}
