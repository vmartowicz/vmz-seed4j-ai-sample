package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

class UnknownReservationException extends UnknownParkingElementException {

  UnknownReservationException(ReservationId reservation) {
    super("Unknown reservation " + reservation.get());
  }
}
