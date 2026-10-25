package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Author;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BookToAdd;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Isbn;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Title;
import org.jspecify.annotations.Nullable;

record RestBookToAdd(@Nullable String isbn, @Nullable String title, @Nullable String author) {
  BookToAdd toDomain() {
    return new BookToAdd(new Isbn(isbn), new Title(title), new Author(author));
  }
}
