package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

public abstract class LaboratoryRuleViolationException extends RuntimeException {

  protected LaboratoryRuleViolationException(String message) {
    super(message);
  }
}
