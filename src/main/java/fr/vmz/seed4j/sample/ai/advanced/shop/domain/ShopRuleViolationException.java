package fr.vmz.seed4j.sample.ai.advanced.shop.domain;

public abstract class ShopRuleViolationException extends RuntimeException {

  protected ShopRuleViolationException(String message) {
    super(message);
  }
}
