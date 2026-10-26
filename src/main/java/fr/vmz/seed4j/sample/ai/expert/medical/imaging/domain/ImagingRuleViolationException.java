package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

public abstract class ImagingRuleViolationException extends RuntimeException {

  protected ImagingRuleViolationException(String message) {
    super(message);
  }
}
