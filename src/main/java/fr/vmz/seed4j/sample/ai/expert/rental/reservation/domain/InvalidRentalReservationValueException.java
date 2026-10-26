package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

public abstract class InvalidRentalReservationValueException extends RuntimeException {

  protected InvalidRentalReservationValueException(String message) {
    super(message);
  }
}
