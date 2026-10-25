package fr.vmz.seed4j.sample.ai.advanced.shop.domain;

import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.MoneyFixture.money;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import org.junit.jupiter.api.Test;

@UnitTest
class MoneyTest {

  @Test
  void shouldNotBuildNegativeMoney() {
    assertThatThrownBy(() -> money("-0.01")).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldComputeAmounts() {
    assertThat(money("10").plus(money("2.5"))).isEqualTo(money("12.50"));
    assertThat(money("10").minus(money("2.5"))).isEqualTo(money("7.50"));
    assertThat(money("2.5").times(3)).isEqualTo(money("7.50"));
  }

  @Test
  void shouldRoundPercentageHalfUp() {
    assertThat(money("33.35").percent(10)).isEqualTo(money("3.34"));
  }

  @Test
  void shouldCompareAmounts() {
    assertThat(money("50.01").isGreaterThan(money("50"))).isTrue();
    assertThat(money("50").isGreaterThan(money("50"))).isFalse();
  }
}
