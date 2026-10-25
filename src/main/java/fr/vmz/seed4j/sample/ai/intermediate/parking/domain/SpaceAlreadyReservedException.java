package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

class SpaceAlreadyReservedException extends ParkingRuleViolationException {

  SpaceAlreadyReservedException(ReservationToCreate reservation) {
    super("Space " + reservation.space().get() + " is already reserved for " + reservation.date());
  }
}
