package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;

@UnitTest
class CustomerEmailTest {

  @Test
  void shouldNotBuildWithoutEmail() {
    assertThatThrownBy(() -> new CustomerEmail(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildInvalidEmail() {
    assertThatThrownBy(() -> new CustomerEmail("jane@shop"))
      .isExactlyInstanceOf(InvalidCustomerEmailException.class)
      .hasMessageContaining("jane@shop");
  }

  @Test
  void shouldNormalizeEmail() {
    assertThat(new CustomerEmail(" Jane@Shop.com ").get()).isEqualTo("jane@shop.com");
  }
}
