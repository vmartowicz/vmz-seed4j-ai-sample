package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;

@UnitTest
class IsbnTest {

  @Test
  void shouldNotBuildWithoutIsbn() {
    assertThatThrownBy(() -> new Isbn(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildIsbnWith12Digits() {
    assertThatThrownBy(() -> new Isbn("978013468599")).isExactlyInstanceOf(InvalidIsbnException.class).hasMessageContaining("978013468599");
  }

  @Test
  void shouldNotBuildIsbnWithHyphens() {
    assertThatThrownBy(() -> new Isbn("978-0134685991")).isExactlyInstanceOf(InvalidIsbnException.class);
  }

  @Test
  void shouldNotBuildIsbnWithWrongCheckDigit() {
    assertThatThrownBy(() -> new Isbn("9780134685992")).isExactlyInstanceOf(InvalidIsbnException.class);
  }

  @Test
  void shouldBuildValidIsbn() {
    assertThat(new Isbn("9780134685991").get()).isEqualTo("9780134685991");
    assertThat(new Isbn("9780306406157").get()).isEqualTo("9780306406157");
  }
}
