package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

public abstract class SponsoringRuleViolationException extends RuntimeException {

  protected SponsoringRuleViolationException(String message) {
    super(message);
  }
}
