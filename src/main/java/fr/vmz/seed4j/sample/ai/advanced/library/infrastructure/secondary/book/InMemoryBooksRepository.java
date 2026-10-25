package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.secondary.book;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Book;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksRepository;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Isbn;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryBooksRepository implements BooksRepository {

  private final Map<Isbn, Book> books = new ConcurrentHashMap<>();

  @Override
  public void save(Book book) {
    books.put(book.isbn(), book);
  }

  @Override
  public Optional<Book> get(Isbn isbn) {
    return Optional.ofNullable(books.get(isbn));
  }

  @Override
  public List<Book> all() {
    return List.copyOf(books.values());
  }
}
