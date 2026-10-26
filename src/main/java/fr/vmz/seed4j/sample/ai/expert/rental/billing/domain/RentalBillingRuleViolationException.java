package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

public abstract class RentalBillingRuleViolationException extends RuntimeException {

  protected RentalBillingRuleViolationException(String message) {
    super(message);
  }
}
