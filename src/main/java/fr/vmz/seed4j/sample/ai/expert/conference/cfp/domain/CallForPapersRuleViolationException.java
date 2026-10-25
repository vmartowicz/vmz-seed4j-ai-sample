package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

public abstract class CallForPapersRuleViolationException extends RuntimeException {

  protected CallForPapersRuleViolationException(String message) {
    super(message);
  }
}
