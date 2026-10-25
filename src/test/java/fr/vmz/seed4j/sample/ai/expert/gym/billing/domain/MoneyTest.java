package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class MoneyTest {

  @Test
  void shouldRoundToCents() {
    assertThat(euros("10.005").get()).isEqualByComparingTo("10.01");
  }

  @Test
  void shouldAddAmounts() {
    assertThat(euros("29.90").plus(euros("10"))).isEqualTo(euros("39.90"));
  }

  @Test
  void shouldComputeProrata() {
    assertThat(euros("29.90").prorata(22, 31)).isEqualTo(euros("21.22"));
  }

  @Test
  void shouldNotBuildNegativeAmount() {
    assertThatThrownBy(() -> new Money(new BigDecimal("-1"))).isInstanceOf(AssertionException.class);
  }
}
