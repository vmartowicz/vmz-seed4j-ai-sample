package fr.vmz.seed4j.sample.ai.advanced.leave.domain;

public abstract class LeaveRuleViolationException extends RuntimeException {

  protected LeaveRuleViolationException(String message) {
    super(message);
  }
}
