package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain;

public abstract class UnknownEcommerceElementException extends RuntimeException {

  protected UnknownEcommerceElementException(String message) {
    super(message);
  }
}
