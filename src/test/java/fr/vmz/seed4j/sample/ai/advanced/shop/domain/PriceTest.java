package fr.vmz.seed4j.sample.ai.advanced.shop.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class PriceTest {

  @Test
  void shouldNotBuildFreePrice() {
    assertThatThrownBy(() -> new Price(BigDecimal.ZERO)).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldNotBuildPriceWithMoreThan2Decimals() {
    assertThatThrownBy(() -> new Price(new BigDecimal("9.999"))).isExactlyInstanceOf(InvalidPriceException.class).hasMessageContaining("9.999");
  }

  @Test
  void shouldMultiplyPriceByQuantity() {
    assertThat(new Price(new BigDecimal("19.9")).get()).isEqualTo(new BigDecimal("19.90"));
    assertThat(new Price(new BigDecimal("19.90")).times(2)).isEqualTo(MoneyFixture.money("39.80"));
  }
}
