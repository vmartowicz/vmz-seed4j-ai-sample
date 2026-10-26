package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

public abstract class LendingRuleViolationException extends RuntimeException {

  protected LendingRuleViolationException(String message) {
    super(message);
  }
}
