package fr.vmz.seed4j.sample.ai.advanced.shop.domain;

public abstract class InvalidShopValueException extends RuntimeException {

  protected InvalidShopValueException(String message) {
    super(message);
  }
}
