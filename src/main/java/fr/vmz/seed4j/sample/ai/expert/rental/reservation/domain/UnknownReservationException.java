package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

class UnknownReservationException extends UnknownRentalReservationElementException {

  UnknownReservationException(ReservationId reservation) {
    super("Unknown reservation " + reservation.get());
  }
}
