package fr.vmz.seed4j.sample.ai.advanced.library.domain;

public abstract class InvalidLibraryValueException extends RuntimeException {

  protected InvalidLibraryValueException(String message) {
    super(message);
  }
}
