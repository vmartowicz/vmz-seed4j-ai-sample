package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain;

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
    assertThatThrownBy(() -> new Price(new BigDecimal("9.999")))
      .isExactlyInstanceOf(InvalidPriceException.class)
      .hasMessageContaining("9.999");
  }

  @Test
  void shouldUseCentsScale() {
    assertThat(new Price(new BigDecimal("10")).get()).isEqualTo(new BigDecimal("10.00"));
  }

  @Test
  void shouldMultiplyByQuantity() {
    assertThat(new Price(new BigDecimal("19.99")).times(3)).isEqualTo(new Money(new BigDecimal("59.97")));
  }
}
