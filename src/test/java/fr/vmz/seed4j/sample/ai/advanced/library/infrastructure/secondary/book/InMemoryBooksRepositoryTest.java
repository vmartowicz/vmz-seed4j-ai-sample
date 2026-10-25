package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.secondary.book;

import static fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryBooksRepositoryTest {

  private final InMemoryBooksRepository books = new InMemoryBooksRepository();

  @Test
  void shouldGetSavedBooks() {
    var effectiveJava = effectiveJava();
    books.save(effectiveJava);

    assertThat(books.get(EFFECTIVE_JAVA)).contains(effectiveJava);
    assertThat(books.get(DDD)).isEmpty();
    assertThat(books.all()).containsExactly(effectiveJava);
  }
}
