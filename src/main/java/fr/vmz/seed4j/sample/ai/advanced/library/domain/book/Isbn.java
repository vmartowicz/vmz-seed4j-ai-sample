package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public record Isbn(String isbn) {
  private static final Pattern FORMAT = Pattern.compile("^\\d{13}$");

  public Isbn {
    Assert.notBlank("isbn", isbn);

    if (!FORMAT.matcher(isbn).matches() || !hasValidCheckDigit(isbn)) {
      throw new InvalidIsbnException(isbn);
    }
  }

  private static boolean hasValidCheckDigit(String isbn) {
    var weightedSum = IntStream.range(0, 13)
      .map(index -> Character.getNumericValue(isbn.charAt(index)) * (index % 2 == 0 ? 1 : 3))
      .sum();

    return weightedSum % 10 == 0;
  }

  public String get() {
    return isbn();
  }
}
