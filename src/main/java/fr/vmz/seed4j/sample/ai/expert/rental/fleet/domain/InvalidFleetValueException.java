package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

public abstract class InvalidFleetValueException extends RuntimeException {

  protected InvalidFleetValueException(String message) {
    super(message);
  }
}
