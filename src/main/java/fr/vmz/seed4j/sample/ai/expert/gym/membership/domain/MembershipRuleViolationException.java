package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

public abstract class MembershipRuleViolationException extends RuntimeException {

  protected MembershipRuleViolationException(String message) {
    super(message);
  }
}
