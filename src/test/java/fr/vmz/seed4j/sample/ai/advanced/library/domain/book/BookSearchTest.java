package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import static fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class BookSearchTest {

  @Test
  void shouldMatchAvailableBooksWithoutCriteria() {
    assertThat(new BookSearch(null, null).matches(effectiveJava())).isTrue();
  }

  @Test
  void shouldMatchTitleContainingCriteriaIgnoringCase() {
    assertThat(new BookSearch(" java ", null).matches(effectiveJava())).isTrue();
    assertThat(new BookSearch("python", null).matches(effectiveJava())).isFalse();
  }

  @Test
  void shouldMatchAuthorContainingCriteriaIgnoringCase() {
    assertThat(new BookSearch(null, "BLOCH").matches(effectiveJava())).isTrue();
    assertThat(new BookSearch("java", "evans").matches(effectiveJava())).isFalse();
  }

  @Test
  void shouldNotMatchUnavailableBook() {
    var book = effectiveJava();
    book.borrow();

    assertThat(new BookSearch(null, null).matches(book)).isFalse();
  }
}
