package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.InvalidLibraryValueException;

class InvalidIsbnException extends InvalidLibraryValueException {

  InvalidIsbnException(String isbn) {
    super("An ISBN has 13 digits and a valid check digit, got: " + isbn);
  }
}
