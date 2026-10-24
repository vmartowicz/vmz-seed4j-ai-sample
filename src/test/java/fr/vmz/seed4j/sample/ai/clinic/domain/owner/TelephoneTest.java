package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;

@UnitTest
class TelephoneTest {

  @Test
  void shouldNotBuildWithoutTelephone() {
    assertThatThrownBy(() -> new Telephone(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWith9Digits() {
    assertThatThrownBy(() -> new Telephone("060855510")).isExactlyInstanceOf(InvalidTelephoneException.class);
  }

  @Test
  void shouldNotBuildWith11Digits() {
    assertThatThrownBy(() -> new Telephone("06085551023")).isExactlyInstanceOf(InvalidTelephoneException.class);
  }

  @Test
  void shouldNotBuildWithNonDigit() {
    assertThatThrownBy(() -> new Telephone("06 0855510"))
      .isExactlyInstanceOf(InvalidTelephoneException.class)
      .hasMessageContaining("06 0855510");
  }

  @Test
  void shouldBuildWith10Digits() {
    assertThat(new Telephone("0608555102").get()).isEqualTo("0608555102");
  }
}
