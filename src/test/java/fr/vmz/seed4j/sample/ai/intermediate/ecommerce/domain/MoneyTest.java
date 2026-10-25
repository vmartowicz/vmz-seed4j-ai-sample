package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class MoneyTest {

  @Test
  void shouldNotBuildNegativeMoney() {
    assertThatThrownBy(() -> new Money(new BigDecimal("-0.01"))).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldAddMoney() {
    assertThat(Money.ZERO.plus(new Money(new BigDecimal("10.5")))).isEqualTo(new Money(new BigDecimal("10.50")));
  }
}
