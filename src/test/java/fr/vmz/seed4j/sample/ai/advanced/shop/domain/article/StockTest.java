package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import org.junit.jupiter.api.Test;

@UnitTest
class StockTest {

  @Test
  void shouldNotBuildNegativeStock() {
    assertThatThrownBy(() -> new Stock(-1)).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldCoverQuantityUpToStock() {
    assertThat(new Stock(3).covers(3)).isTrue();
    assertThat(new Stock(3).covers(4)).isFalse();
  }

  @Test
  void shouldAlertBelow5Units() {
    assertThat(new Stock(4).isLow()).isTrue();
    assertThat(new Stock(5).isLow()).isFalse();
  }

  @Test
  void shouldRemoveAndAddUnits() {
    assertThat(new Stock(3).minus(2)).isEqualTo(new Stock(1));
    assertThat(new Stock(3).plus(2).get()).isEqualTo(5);
  }
}
