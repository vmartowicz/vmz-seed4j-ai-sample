package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

public abstract class UnknownParkingElementException extends RuntimeException {

  protected UnknownParkingElementException(String message) {
    super(message);
  }
}
