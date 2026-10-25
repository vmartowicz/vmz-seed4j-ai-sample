package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

class ReservationAlreadyCancelledException extends ParkingRuleViolationException {

  ReservationAlreadyCancelledException(ReservationId reservation) {
    super("This reservation is already cancelled: " + reservation.get());
  }
}
