package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

public abstract class ParkingRuleViolationException extends RuntimeException {

  protected ParkingRuleViolationException(String message) {
    super(message);
  }
}
