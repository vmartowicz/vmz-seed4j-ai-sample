package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.LibraryRuleViolationException;

class BookNotInMaintenanceException extends LibraryRuleViolationException {

  BookNotInMaintenanceException(Isbn isbn) {
    super("Book " + isbn.get() + " is not in maintenance");
  }
}
