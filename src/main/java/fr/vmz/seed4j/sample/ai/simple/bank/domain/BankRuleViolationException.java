package fr.vmz.seed4j.sample.ai.simple.bank.domain;

public abstract class BankRuleViolationException extends RuntimeException {

  protected BankRuleViolationException(String message) {
    super(message);
  }
}
