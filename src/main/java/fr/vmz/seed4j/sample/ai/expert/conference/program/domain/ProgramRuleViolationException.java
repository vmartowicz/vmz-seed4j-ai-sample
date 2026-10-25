package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

public abstract class ProgramRuleViolationException extends RuntimeException {

  protected ProgramRuleViolationException(String message) {
    super(message);
  }
}
