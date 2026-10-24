package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class LastNameTest {

  @Test
  void shouldNotBuildWithoutLastName() {
    assertThatThrownBy(() -> new LastName(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan30Characters() {
    assertThatThrownBy(() -> new LastName("a".repeat(31))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldBuildWith30Characters() {
    assertThat(new LastName("a".repeat(30)).get()).isEqualTo("a".repeat(30));
  }
}
