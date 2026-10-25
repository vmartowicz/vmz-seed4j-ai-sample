package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class BookValuesTest {

  @Test
  void shouldNotBuildTitleWithMoreThan200Characters() {
    assertThatThrownBy(() -> new Title("a".repeat(201))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldNotBuildBlankAuthor() {
    assertThatThrownBy(() -> new Author(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldExposeValues() {
    assertThat(new Title("Effective Java").get()).isEqualTo("Effective Java");
    assertThat(new Author("Joshua Bloch").get()).isEqualTo("Joshua Bloch");
  }
}
