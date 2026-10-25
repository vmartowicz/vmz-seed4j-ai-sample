package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import java.util.List;
import java.util.Optional;

public interface BooksRepository {
  void save(Book book);

  Optional<Book> get(Isbn isbn);

  List<Book> all();

  default Book book(Isbn isbn) {
    return get(isbn).orElseThrow(() -> new UnknownBookException(isbn));
  }
}
