package fr.vmz.seed4j.sample.ai.shared.rental.domain;

public final class InvalidEmailException extends RuntimeException {

  InvalidEmailException(String email) {
    super("Invalid email: " + email);
  }
}
