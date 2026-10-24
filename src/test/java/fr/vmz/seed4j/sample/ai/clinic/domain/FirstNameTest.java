package fr.vmz.seed4j.sample.ai.clinic.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class FirstNameTest {

  @Test
  void shouldNotBuildWithoutFirstName() {
    assertThatThrownBy(() -> new FirstName(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan30Characters() {
    assertThatThrownBy(() -> new FirstName("a".repeat(31))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldBuildWith30Characters() {
    assertThat(new FirstName("a".repeat(30)).get()).isEqualTo("a".repeat(30));
  }
}
