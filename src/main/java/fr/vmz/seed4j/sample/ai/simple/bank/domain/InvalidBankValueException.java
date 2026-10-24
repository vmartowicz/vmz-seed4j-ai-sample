package fr.vmz.seed4j.sample.ai.simple.bank.domain;

public abstract class InvalidBankValueException extends RuntimeException {

  protected InvalidBankValueException(String message) {
    super(message);
  }
}
