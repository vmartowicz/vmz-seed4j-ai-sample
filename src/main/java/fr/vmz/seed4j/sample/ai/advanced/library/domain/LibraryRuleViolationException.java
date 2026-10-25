package fr.vmz.seed4j.sample.ai.advanced.library.domain;

public abstract class LibraryRuleViolationException extends RuntimeException {

  protected LibraryRuleViolationException(String message) {
    super(message);
  }
}
