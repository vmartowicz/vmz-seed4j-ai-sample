package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

public abstract class AccessRuleViolationException extends RuntimeException {

  protected AccessRuleViolationException(String message) {
    super(message);
  }
}
