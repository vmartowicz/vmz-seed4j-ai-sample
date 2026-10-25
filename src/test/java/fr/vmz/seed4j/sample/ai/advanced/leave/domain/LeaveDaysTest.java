package fr.vmz.seed4j.sample.ai.advanced.leave.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@UnitTest
class LeaveDaysTest {

  @Test
  void shouldNotBuildNegativeDays() {
    assertThatThrownBy(() -> days("-0.5")).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldNotBuildDaysNotCountedInHalfDays() {
    assertThatThrownBy(() -> days("0.3")).isExactlyInstanceOf(InvalidLeaveDaysException.class).hasMessageContaining("0.3");
  }

  @Test
  void shouldBuildHalfDay() {
    assertThat(days("0.5").get()).isEqualTo(new BigDecimal("0.5"));
    assertThat(LeaveDays.ofHalfDays(3)).isEqualTo(days("1.5"));
  }

  @Test
  void shouldGrant25DaysPerYear() {
    assertThat(LeaveDays.YEARLY_ENTITLEMENT).isEqualTo(days("25"));
  }

  @Test
  void shouldAddAndSubtractDays() {
    assertThat(days("2.5").plus(days("1"))).isEqualTo(days("3.5"));
    assertThat(days("2.5").minus(days("0.5"))).isEqualTo(days("2"));
  }

  @Test
  void shouldCoverEqualDays() {
    assertThat(days("2.5").covers(days("2.5"))).isTrue();
    assertThat(days("2.5").covers(days("3"))).isFalse();
  }

  private static LeaveDays days(String value) {
    return new LeaveDays(new BigDecimal(value));
  }
}
