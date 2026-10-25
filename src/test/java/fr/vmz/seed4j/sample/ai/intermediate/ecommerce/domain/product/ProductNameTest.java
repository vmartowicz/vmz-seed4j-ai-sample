package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class ProductNameTest {

  @Test
  void shouldNotBuildWithoutName() {
    assertThatThrownBy(() -> new ProductName(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan100Characters() {
    assertThatThrownBy(() -> new ProductName("a".repeat(101))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldBuildTrimmedName() {
    assertThat(new ProductName(" T-shirt ").get()).isEqualTo("T-shirt");
  }

  @Test
  void shouldBeSameNameIgnoringCase() {
    assertThat(new ProductName("T-shirt").sameAs(new ProductName("t-SHIRT"))).isTrue();
  }

  @Test
  void shouldNotBeSameAsAnotherName() {
    assertThat(new ProductName("T-shirt").sameAs(new ProductName("Mug"))).isFalse();
  }
}
