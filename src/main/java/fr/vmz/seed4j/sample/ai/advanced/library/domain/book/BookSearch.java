package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import java.util.Locale;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public record BookSearch(String title, String author) {
  public BookSearch(@Nullable String title, @Nullable String author) {
    this.title = normalized(title);
    this.author = normalized(author);
  }

  private static String normalized(@Nullable String criteria) {
    return Objects.requireNonNullElse(criteria, "").trim().toLowerCase(Locale.ROOT);
  }

  public boolean matches(Book book) {
    return (
      book.status() == BookStatus.AVAILABLE &&
      book.title().get().toLowerCase(Locale.ROOT).contains(title) &&
      book.author().get().toLowerCase(Locale.ROOT).contains(author)
    );
  }
}
