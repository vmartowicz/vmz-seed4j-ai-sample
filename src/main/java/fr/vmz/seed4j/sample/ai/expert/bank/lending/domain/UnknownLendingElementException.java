package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

public abstract class UnknownLendingElementException extends RuntimeException {

  protected UnknownLendingElementException(String message) {
    super(message);
  }
}
