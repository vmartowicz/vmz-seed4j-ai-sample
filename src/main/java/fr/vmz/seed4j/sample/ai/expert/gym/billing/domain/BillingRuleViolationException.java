package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

public abstract class BillingRuleViolationException extends RuntimeException {

  protected BillingRuleViolationException(String message) {
    super(message);
  }
}
