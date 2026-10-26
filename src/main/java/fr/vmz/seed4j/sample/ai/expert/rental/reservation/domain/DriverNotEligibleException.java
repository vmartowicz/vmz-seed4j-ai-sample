package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

class DriverNotEligibleException extends RentalReservationRuleViolationException {

  DriverNotEligibleException(String reason) {
    super(reason);
  }
}
