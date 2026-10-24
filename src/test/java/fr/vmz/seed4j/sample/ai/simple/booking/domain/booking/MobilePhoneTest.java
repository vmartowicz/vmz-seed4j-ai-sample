package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;

@UnitTest
class MobilePhoneTest {

  @Test
  void shouldNotBuildWithoutPhone() {
    assertThatThrownBy(() -> new MobilePhone(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildLandlinePhone() {
    assertThatThrownBy(() -> new MobilePhone("0512345678"))
      .isExactlyInstanceOf(InvalidMobilePhoneException.class)
      .hasMessageContaining("0512345678");
  }

  @Test
  void shouldNotBuildWith9Digits() {
    assertThatThrownBy(() -> new MobilePhone("061234567")).isExactlyInstanceOf(InvalidMobilePhoneException.class);
  }

  @Test
  void shouldBuildMobilePhoneStartingWith06() {
    assertThat(new MobilePhone("0612345678").get()).isEqualTo("0612345678");
  }

  @Test
  void shouldBuildMobilePhoneStartingWith07() {
    assertThat(new MobilePhone("0712345678").get()).isEqualTo("0712345678");
  }
}
