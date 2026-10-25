package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

public abstract class UnknownBillingElementException extends RuntimeException {

  protected UnknownBillingElementException(String message) {
    super(message);
  }
}
