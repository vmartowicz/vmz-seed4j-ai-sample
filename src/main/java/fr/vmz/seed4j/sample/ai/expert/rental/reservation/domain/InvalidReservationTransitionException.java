package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

class InvalidReservationTransitionException extends RentalReservationRuleViolationException {

  InvalidReservationTransitionException(ReservationId reservation, ReservationStatus status, String action) {
    super("Can not " + action + " reservation " + reservation.get() + " which is " + status);
  }
}
