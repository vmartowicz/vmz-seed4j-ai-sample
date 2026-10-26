package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

public abstract class UnknownPaymentsElementException extends RuntimeException {

  protected UnknownPaymentsElementException(String message) {
    super(message);
  }
}
