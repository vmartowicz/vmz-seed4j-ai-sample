package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

public abstract class UnknownRentalReservationElementException extends RuntimeException {

  protected UnknownRentalReservationElementException(String message) {
    super(message);
  }
}
