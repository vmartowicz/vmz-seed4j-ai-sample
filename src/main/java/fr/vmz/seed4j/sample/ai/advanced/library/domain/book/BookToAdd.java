package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record BookToAdd(Isbn isbn, Title title, Author author) {
  public BookToAdd {
    Assert.notNull("isbn", isbn);
    Assert.notNull("title", title);
    Assert.notNull("author", author);
  }

  public Book add() {
    return new Book(this);
  }
}
