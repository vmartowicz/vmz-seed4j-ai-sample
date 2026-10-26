package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

public abstract class UnknownRentalBillingElementException extends RuntimeException {

  protected UnknownRentalBillingElementException(String message) {
    super(message);
  }
}
