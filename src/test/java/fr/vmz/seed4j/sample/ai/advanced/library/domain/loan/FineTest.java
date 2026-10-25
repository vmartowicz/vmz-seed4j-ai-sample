package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class FineTest {

  @Test
  void shouldNotBuildNegativeFine() {
    assertThatThrownBy(() -> fine("-0.50")).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldCharge50CentsPerLateDay() {
    assertThat(Fine.forLateDays(0)).isEqualTo(Fine.NONE);
    assertThat(Fine.forLateDays(3)).isEqualTo(fine("1.50"));
  }

  @Test
  void shouldAddFines() {
    assertThat(fine("9.50").plus(fine("0.5"))).isEqualTo(fine("10.00"));
  }

  @Test
  void shouldExceedOnlyStrictlyHigherAmount() {
    assertThat(fine("10.50").exceeds(fine("10"))).isTrue();
    assertThat(fine("10").exceeds(fine("10"))).isFalse();
  }

  @Test
  void shouldKnowIfThereIsSomethingToPay() {
    assertThat(fine("0.50").isDue()).isTrue();
    assertThat(Fine.NONE.isDue()).isFalse();
  }

  private static Fine fine(String amount) {
    return new Fine(new BigDecimal(amount));
  }
}
