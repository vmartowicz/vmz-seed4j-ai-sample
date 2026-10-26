package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

public abstract class CounterRuleViolationException extends RuntimeException {

  protected CounterRuleViolationException(String message) {
    super(message);
  }
}
