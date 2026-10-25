package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import static fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class BookTest {

  @Test
  void shouldAddAvailableBook() {
    var book = effectiveJava();

    assertThat(book.isbn()).isEqualTo(EFFECTIVE_JAVA);
    assertThat(book.title()).isEqualTo(new Title("Effective Java"));
    assertThat(book.author()).isEqualTo(new Author("Joshua Bloch"));
    assertThat(book.status()).isEqualTo(BookStatus.AVAILABLE);
  }

  @Test
  void shouldBorrowAndGiveBackBook() {
    var book = effectiveJava();

    book.borrow();
    assertThat(book.status()).isEqualTo(BookStatus.BORROWED);

    book.giveBack();
    assertThat(book.status()).isEqualTo(BookStatus.AVAILABLE);
  }

  @Test
  void shouldNotBorrowBorrowedBook() {
    var book = effectiveJava();
    book.borrow();

    assertThatThrownBy(book::borrow).isExactlyInstanceOf(BookNotAvailableException.class).hasMessageContaining("9780134685991");
  }

  @Test
  void shouldPutBookInMaintenanceAndBack() {
    var book = effectiveJava();

    book.startMaintenance();
    assertThat(book.status()).isEqualTo(BookStatus.MAINTENANCE);
    assertThatThrownBy(book::borrow).isExactlyInstanceOf(BookNotAvailableException.class);

    book.endMaintenance();
    assertThat(book.status()).isEqualTo(BookStatus.AVAILABLE);
  }

  @Test
  void shouldNotPutBorrowedBookInMaintenance() {
    var book = effectiveJava();
    book.borrow();

    assertThatThrownBy(book::startMaintenance).isExactlyInstanceOf(BookNotAvailableException.class);
  }

  @Test
  void shouldNotEndMaintenanceOfBookNotInMaintenance() {
    assertThatThrownBy(effectiveJava()::endMaintenance)
      .isExactlyInstanceOf(BookNotInMaintenanceException.class)
      .hasMessageContaining("9780134685991");
  }
}
