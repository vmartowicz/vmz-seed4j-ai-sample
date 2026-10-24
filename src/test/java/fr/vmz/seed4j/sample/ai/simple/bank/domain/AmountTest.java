package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import static fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class AmountTest {

  @Test
  void shouldNotBuildWithoutAmount() {
    assertThatThrownBy(() -> new Amount(null)).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildZeroAmount() {
    assertThatThrownBy(() -> amount("0.00")).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldNotBuildNegativeAmount() {
    assertThatThrownBy(() -> amount("-10")).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldNotBuildAmountWithMoreThan2Decimals() {
    assertThatThrownBy(() -> amount("10.005")).isExactlyInstanceOf(InvalidAmountException.class).hasMessageContaining("10.005");
  }

  @Test
  void shouldBuildSmallestAmount() {
    assertThat(amount("0.01").get()).isEqualTo(new BigDecimal("0.01"));
  }

  @Test
  void shouldUseCentsScale() {
    assertThat(amount("10").get()).isEqualTo(new BigDecimal("10.00"));
    assertThat(amount("10.500").get()).isEqualTo(new BigDecimal("10.50"));
  }
}
