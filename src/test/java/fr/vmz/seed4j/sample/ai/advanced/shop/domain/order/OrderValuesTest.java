package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class OrderValuesTest {

  @Test
  void shouldNotBuildZeroQuantity() {
    assertThatThrownBy(() -> new Quantity(0)).isExactlyInstanceOf(NumberValueTooLowException.class);
    assertThat(new Quantity(1).get()).isEqualTo(1);
  }

  @Test
  void shouldNotBuildInvalidCustomerEmail() {
    assertThatThrownBy(() -> new CustomerEmail(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
    assertThatThrownBy(() -> new CustomerEmail("jane@shop"))
      .isExactlyInstanceOf(InvalidCustomerEmailException.class)
      .hasMessageContaining("jane@shop");
    assertThat(new CustomerEmail(" Jane@Shop.com ").get()).isEqualTo("jane@shop.com");
  }

  @Test
  void shouldNotBuildTrackingNumberWithMoreThan50Characters() {
    assertThatThrownBy(() -> new TrackingNumber(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
    assertThatThrownBy(() -> new TrackingNumber("a".repeat(51))).isExactlyInstanceOf(StringTooLongException.class);
    assertThat(new TrackingNumber("6A12345678901").get()).isEqualTo("6A12345678901");
  }
}
