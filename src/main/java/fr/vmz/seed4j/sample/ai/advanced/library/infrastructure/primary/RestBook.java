package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Book;

record RestBook(String isbn, String title, String author, String status) {
  static RestBook from(Book book) {
    return new RestBook(book.isbn().get(), book.title().get(), book.author().get(), book.status().name());
  }
}
