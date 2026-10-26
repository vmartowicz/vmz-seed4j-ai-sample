package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

public abstract class RentalReservationRuleViolationException extends RuntimeException {

  protected RentalReservationRuleViolationException(String message) {
    super(message);
  }
}
