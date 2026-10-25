package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.LibraryRuleViolationException;

public class BookNotAvailableException extends LibraryRuleViolationException {

  BookNotAvailableException(Isbn isbn, BookStatus status) {
    super("Book " + isbn.get() + " is not available, it is " + status);
  }
}
