package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

class CancellationDeadlinePassedException extends ParkingRuleViolationException {

  CancellationDeadlinePassedException(ReservationId reservation) {
    super("A reservation can only be cancelled up to 24 hours before its day: " + reservation.get());
  }
}
