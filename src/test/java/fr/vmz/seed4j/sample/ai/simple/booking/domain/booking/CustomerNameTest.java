package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class CustomerNameTest {

  @Test
  void shouldNotBuildWithoutName() {
    assertThatThrownBy(() -> new CustomerName(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan100Characters() {
    assertThatThrownBy(() -> new CustomerName("a".repeat(101))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldBuildWith100Characters() {
    assertThat(new CustomerName("a".repeat(100)).get()).isEqualTo("a".repeat(100));
  }
}
