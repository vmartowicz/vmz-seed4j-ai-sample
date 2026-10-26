package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

public abstract class FleetRuleViolationException extends RuntimeException {

  protected FleetRuleViolationException(String message) {
    super(message);
  }
}
