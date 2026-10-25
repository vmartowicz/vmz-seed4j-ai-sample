package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import org.junit.jupiter.api.Test;

@UnitTest
class QuantityTest {

  @Test
  void shouldNotBuildZeroQuantity() {
    assertThatThrownBy(() -> new Quantity(0)).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldBuildQuantityOfOne() {
    assertThat(new Quantity(1).get()).isEqualTo(1);
  }
}
