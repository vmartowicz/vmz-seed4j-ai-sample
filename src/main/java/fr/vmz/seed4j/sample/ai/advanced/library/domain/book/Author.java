package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Author(String author) {
  public Author {
    Assert.field("author", author).notBlank().maxLength(100);
  }

  public String get() {
    return author();
  }
}
