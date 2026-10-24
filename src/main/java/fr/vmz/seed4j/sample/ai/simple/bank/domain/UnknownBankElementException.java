package fr.vmz.seed4j.sample.ai.simple.bank.domain;

public abstract class UnknownBankElementException extends RuntimeException {

  protected UnknownBankElementException(String message) {
    super(message);
  }
}
