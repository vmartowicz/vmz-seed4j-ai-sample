package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

public abstract class UnknownCounterElementException extends RuntimeException {

  protected UnknownCounterElementException(String message) {
    super(message);
  }
}
