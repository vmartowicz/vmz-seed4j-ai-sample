package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.LibraryRuleViolationException;

class DuplicatedIsbnException extends LibraryRuleViolationException {

  DuplicatedIsbnException(Isbn isbn) {
    super("A book with ISBN " + isbn.get() + " is already in the catalog");
  }
}
