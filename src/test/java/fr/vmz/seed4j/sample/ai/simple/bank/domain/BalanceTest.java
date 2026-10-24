package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import static fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class BalanceTest {

  @Test
  void shouldNotBuildNegativeBalance() {
    assertThatThrownBy(() -> balance("-0.01")).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldStartAtZero() {
    assertThat(Balance.ZERO.get()).isEqualTo(new BigDecimal("0.00"));
  }

  @Test
  void shouldAddAmount() {
    assertThat(balance("10.50").plus(amount("0.50"))).isEqualTo(balance("11.00"));
  }

  @Test
  void shouldSubtractAmount() {
    assertThat(balance("10.50").minus(amount("10.50"))).isEqualTo(Balance.ZERO);
  }

  @Test
  void shouldCoverAmountEqualToBalance() {
    assertThat(balance("10.50").covers(amount("10.50"))).isTrue();
  }

  @Test
  void shouldNotCoverAmountAboveBalance() {
    assertThat(balance("10.50").covers(amount("10.51"))).isFalse();
  }
}
