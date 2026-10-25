package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import static fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class BooksCatalogTest {

  private final BooksRepository books = mock(BooksRepository.class, CALLS_REAL_METHODS);
  private final BooksCatalog catalog = new BooksCatalog(books);

  @Test
  void shouldAddBook() {
    doReturn(Optional.empty()).when(books).get(EFFECTIVE_JAVA);

    var book = catalog.add(new BookToAdd(EFFECTIVE_JAVA, new Title("Effective Java"), new Author("Joshua Bloch")));

    assertThat(book.status()).isEqualTo(BookStatus.AVAILABLE);
    verify(books).save(book);
  }

  @Test
  void shouldNotAddTwoBooksWithTheSameIsbn() {
    doReturn(Optional.of(effectiveJava())).when(books).get(EFFECTIVE_JAVA);

    assertThatThrownBy(() -> catalog.add(new BookToAdd(EFFECTIVE_JAVA, new Title("Other"), new Author("Other"))))
      .isExactlyInstanceOf(DuplicatedIsbnException.class)
      .hasMessageContaining("9780134685991");
    verify(books, never()).save(any());
  }

  @Test
  void shouldSearchAvailableBooksByTitle() {
    var borrowed = book(new Isbn("9780306406157"), "Another Java book", "Someone");
    borrowed.borrow();
    doReturn(List.of(effectiveJava(), ddd(), borrowed, book(new Isbn("9781861972712"), "Advanced Java", "Someone"))).when(books).all();

    assertThat(catalog.search(new BookSearch(null, null)))
      .extracting(book -> book.title().get())
      .containsExactly("Advanced Java", "Domain-Driven Design", "Effective Java");
  }

  @Test
  void shouldPutBookInMaintenanceAndBack() {
    var book = effectiveJava();
    doReturn(Optional.of(book)).when(books).get(EFFECTIVE_JAVA);

    catalog.startMaintenance(EFFECTIVE_JAVA);
    assertThat(book.status()).isEqualTo(BookStatus.MAINTENANCE);

    catalog.endMaintenance(EFFECTIVE_JAVA);
    assertThat(book.status()).isEqualTo(BookStatus.AVAILABLE);
    verify(books, times(2)).save(book);
  }

  @Test
  void shouldNotHandleUnknownBook() {
    doReturn(Optional.empty()).when(books).get(DDD);

    assertThatThrownBy(() -> catalog.startMaintenance(DDD)).isExactlyInstanceOf(UnknownBookException.class).hasMessageContaining("9780321125217");
  }
}
