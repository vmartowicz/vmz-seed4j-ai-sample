package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

public abstract class MedicalRecordsRuleViolationException extends RuntimeException {

  protected MedicalRecordsRuleViolationException(String message) {
    super(message);
  }
}
