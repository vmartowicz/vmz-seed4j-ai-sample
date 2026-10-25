package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.UnknownLibraryElementException;

public class UnknownBookException extends UnknownLibraryElementException {

  UnknownBookException(Isbn isbn) {
    super("Unknown book " + isbn.get());
  }
}
