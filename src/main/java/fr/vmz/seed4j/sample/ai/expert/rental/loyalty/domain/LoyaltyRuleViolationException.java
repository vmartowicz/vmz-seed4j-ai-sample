package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain;

public abstract class LoyaltyRuleViolationException extends RuntimeException {

  protected LoyaltyRuleViolationException(String message) {
    super(message);
  }
}
