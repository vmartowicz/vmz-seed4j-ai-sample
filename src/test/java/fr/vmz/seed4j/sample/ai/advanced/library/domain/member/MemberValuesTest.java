package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class MemberValuesTest {

  @Test
  void shouldNotBuildFullNameWithMoreThan100Characters() {
    assertThatThrownBy(() -> new FullName("a".repeat(101))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldNotBuildBlankEmail() {
    assertThatThrownBy(() -> new MemberEmail(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildInvalidEmail() {
    assertThatThrownBy(() -> new MemberEmail("jane@mail"))
      .isExactlyInstanceOf(InvalidMemberEmailException.class)
      .hasMessageContaining("jane@mail");
  }

  @Test
  void shouldNormalizeEmail() {
    assertThat(new MemberEmail(" Jane@Mail.com ").get()).isEqualTo("jane@mail.com");
    assertThat(new FullName("Jane Doe").get()).isEqualTo("Jane Doe");
  }
}
