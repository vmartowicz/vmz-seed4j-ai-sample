package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain;

public abstract class EcommerceRuleViolationException extends RuntimeException {

  protected EcommerceRuleViolationException(String message) {
    super(message);
  }
}
