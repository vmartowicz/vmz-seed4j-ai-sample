package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class CityTest {

  @Test
  void shouldNotBuildWithoutCity() {
    assertThatThrownBy(() -> new City(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan80Characters() {
    assertThatThrownBy(() -> new City("a".repeat(81))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldBuildWith80Characters() {
    assertThat(new City("a".repeat(80)).get()).isEqualTo("a".repeat(80));
  }
}
