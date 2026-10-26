package fr.vmz.seed4j.sample.ai.shared.rental.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class RentalKernelTest {

  @Test
  void shouldNormalizeEmail() {
    assertThat(new Email(" Jane@Mail.COM ").get()).isEqualTo("jane@mail.com");
  }

  @Test
  void shouldNotBuildInvalidEmail() {
    assertThatThrownBy(() -> new Email("jane@mail")).isExactlyInstanceOf(InvalidEmailException.class);
  }

  @Test
  void shouldUpgradeAlongTheLadder() {
    assertThat(Category.ECO.upgrades()).containsExactly(Category.COMFORT, Category.SUV, Category.PREMIUM);
    assertThat(Category.SUV.upgrades()).containsExactly(Category.PREMIUM);
    assertThat(Category.PREMIUM.upgrades()).isEmpty();
  }

  @Test
  void shouldNotUpgradeVans() {
    assertThat(Category.VAN.upgrades()).isEmpty();
  }

  @Test
  void shouldForbidPremiumAndVansToYoungDrivers() {
    assertThat(Category.PREMIUM.isForbiddenToYoungDrivers()).isTrue();
    assertThat(Category.VAN.isForbiddenToYoungDrivers()).isTrue();
    assertThat(Category.SUV.isForbiddenToYoungDrivers()).isFalse();
  }
}
