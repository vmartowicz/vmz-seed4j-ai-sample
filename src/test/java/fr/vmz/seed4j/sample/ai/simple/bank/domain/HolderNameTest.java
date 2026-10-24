package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class HolderNameTest {

  @Test
  void shouldNotBuildWithoutName() {
    assertThatThrownBy(() -> new HolderName(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan100Characters() {
    assertThatThrownBy(() -> new HolderName("a".repeat(101))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldBuildWith100Characters() {
    assertThat(new HolderName("a".repeat(100)).get()).isEqualTo("a".repeat(100));
  }
}
