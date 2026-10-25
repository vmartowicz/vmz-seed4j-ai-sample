package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;

@UnitTest
class EmployeeEmailTest {

  @Test
  void shouldNotBuildWithoutEmail() {
    assertThatThrownBy(() -> new EmployeeEmail(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithoutAt() {
    assertThatThrownBy(() -> new EmployeeEmail("jane.corp.com"))
      .isExactlyInstanceOf(InvalidEmployeeEmailException.class)
      .hasMessageContaining("jane.corp.com");
  }

  @Test
  void shouldNotBuildWithoutDomainExtension() {
    assertThatThrownBy(() -> new EmployeeEmail("jane@corp")).isExactlyInstanceOf(InvalidEmployeeEmailException.class);
  }

  @Test
  void shouldNormalizeEmail() {
    assertThat(new EmployeeEmail(" Jane.Doe@Corp.com ").get()).isEqualTo("jane.doe@corp.com");
  }
}
