package fr.vmz.seed4j.sample.ai.advanced.library.application;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Book;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BookSearch;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BookToAdd;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksCatalog;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksRepository;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Isbn;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BooksApplicationService {

  private final BooksCatalog catalog;

  public BooksApplicationService(BooksRepository books) {
    catalog = new BooksCatalog(books);
  }

  public Book add(BookToAdd book) {
    return catalog.add(book);
  }

  public List<Book> search(BookSearch search) {
    return catalog.search(search);
  }

  public Book startMaintenance(Isbn isbn) {
    return catalog.startMaintenance(isbn);
  }

  public Book endMaintenance(Isbn isbn) {
    return catalog.endMaintenance(isbn);
  }
}
