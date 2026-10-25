package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

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
  void shouldBuildEmptyStock() {
    assertThat(new Stock(0).get()).isZero();
  }

  @Test
  void shouldCoverQuantityEqualToStock() {
    assertThat(new Stock(3).covers(3)).isTrue();
  }

  @Test
  void shouldNotCoverQuantityAboveStock() {
    assertThat(new Stock(3).covers(4)).isFalse();
  }

  @Test
  void shouldRemoveAndAddQuantities() {
    assertThat(new Stock(3).minus(2)).isEqualTo(new Stock(1));
    assertThat(new Stock(3).plus(2)).isEqualTo(new Stock(5));
  }
}
