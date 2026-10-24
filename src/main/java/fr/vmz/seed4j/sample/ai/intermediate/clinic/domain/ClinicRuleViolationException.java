package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain;

public abstract class ClinicRuleViolationException extends RuntimeException {

  protected ClinicRuleViolationException(String message) {
    super(message);
  }
}
