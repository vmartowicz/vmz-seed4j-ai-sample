package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain;

public abstract class InvalidEcommerceValueException extends RuntimeException {

  protected InvalidEcommerceValueException(String message) {
    super(message);
  }
}
