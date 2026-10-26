package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

public abstract class RetailAccountsRuleViolationException extends RuntimeException {

  protected RetailAccountsRuleViolationException(String message) {
    super(message);
  }
}
