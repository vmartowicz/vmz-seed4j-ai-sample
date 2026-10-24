package fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class ServiceNameTest {

  @Test
  void shouldNotBuildWithoutName() {
    assertThatThrownBy(() -> new ServiceName(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan50Characters() {
    assertThatThrownBy(() -> new ServiceName("a".repeat(51))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldBuildWith50Characters() {
    assertThat(new ServiceName("a".repeat(50)).get()).isEqualTo("a".repeat(50));
  }
}
