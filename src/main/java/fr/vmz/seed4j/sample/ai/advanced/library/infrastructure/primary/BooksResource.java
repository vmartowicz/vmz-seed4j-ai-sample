package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.application.BooksApplicationService;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BookSearch;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Isbn;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library/books")
class BooksResource {

  private final BooksApplicationService books;

  BooksResource(BooksApplicationService books) {
    this.books = books;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestBook add(@RequestBody RestBookToAdd book) {
    return RestBook.from(books.add(book.toDomain()));
  }

  @GetMapping("/available")
  List<RestBook> available(@RequestParam(required = false) @Nullable String title, @RequestParam(required = false) @Nullable String author) {
    return books.search(new BookSearch(title, author)).stream().map(RestBook::from).toList();
  }

  @PostMapping("/{isbn}/maintenance")
  RestBook startMaintenance(@PathVariable String isbn) {
    return RestBook.from(books.startMaintenance(new Isbn(isbn)));
  }

  @DeleteMapping("/{isbn}/maintenance")
  RestBook endMaintenance(@PathVariable String isbn) {
    return RestBook.from(books.endMaintenance(new Isbn(isbn)));
  }
}
