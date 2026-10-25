package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class BooksCatalog {

  private static final Comparator<Book> BY_TITLE = Comparator.comparing(book -> book.title().get(), String.CASE_INSENSITIVE_ORDER);

  private final BooksRepository books;

  public BooksCatalog(BooksRepository books) {
    this.books = books;
  }

  public Book add(BookToAdd toAdd) {
    Assert.notNull("toAdd", toAdd);

    if (books.get(toAdd.isbn()).isPresent()) {
      throw new DuplicatedIsbnException(toAdd.isbn());
    }

    var book = toAdd.add();
    books.save(book);

    return book;
  }

  public List<Book> search(BookSearch search) {
    Assert.notNull("search", search);

    return books.all().stream().filter(search::matches).sorted(BY_TITLE).toList();
  }

  public Book startMaintenance(Isbn isbn) {
    return change(isbn, Book::startMaintenance);
  }

  public Book endMaintenance(Isbn isbn) {
    return change(isbn, Book::endMaintenance);
  }

  private Book change(Isbn isbn, Consumer<Book> change) {
    var book = books.book(isbn);
    change.accept(book);
    books.save(book);

    return book;
  }
}
