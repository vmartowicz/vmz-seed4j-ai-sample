package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

public abstract class UnknownMedicalBillingElementException extends RuntimeException {

  protected UnknownMedicalBillingElementException(String message) {
    super(message);
  }
}
