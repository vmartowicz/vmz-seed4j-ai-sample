package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

public abstract class UnknownFleetElementException extends RuntimeException {

  protected UnknownFleetElementException(String message) {
    super(message);
  }
}
