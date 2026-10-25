package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import java.time.LocalDate;

class ReservationOutOfWindowException extends ParkingRuleViolationException {

  ReservationOutOfWindowException(LocalDate date) {
    super("A space can be reserved from today up to 7 days in advance, not for " + date);
  }
}
