package fr.vmz.seed4j.sample.ai.shared.bank.domain;

public final class InvalidIbanException extends RuntimeException {

  InvalidIbanException(String iban) {
    super("Invalid French IBAN: " + iban);
  }
}
