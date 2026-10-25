package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

public abstract class OnsiteRuleViolationException extends RuntimeException {

  protected OnsiteRuleViolationException(String message) {
    super(message);
  }
}
