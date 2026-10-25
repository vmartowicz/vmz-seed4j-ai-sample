package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

public final class Book {

  private final Isbn isbn;
  private final Title title;
  private final Author author;
  private BookStatus status = BookStatus.AVAILABLE;

  Book(BookToAdd book) {
    isbn = book.isbn();
    title = book.title();
    author = book.author();
  }

  public void borrow() {
    assertAvailable();

    status = BookStatus.BORROWED;
  }

  public void giveBack() {
    status = BookStatus.AVAILABLE;
  }

  public void startMaintenance() {
    assertAvailable();

    status = BookStatus.MAINTENANCE;
  }

  public void endMaintenance() {
    if (status != BookStatus.MAINTENANCE) {
      throw new BookNotInMaintenanceException(isbn);
    }

    status = BookStatus.AVAILABLE;
  }

  private void assertAvailable() {
    if (status != BookStatus.AVAILABLE) {
      throw new BookNotAvailableException(isbn, status);
    }
  }

  public Isbn isbn() {
    return isbn;
  }

  public Title title() {
    return title;
  }

  public Author author() {
    return author;
  }

  public BookStatus status() {
    return status;
  }
}
