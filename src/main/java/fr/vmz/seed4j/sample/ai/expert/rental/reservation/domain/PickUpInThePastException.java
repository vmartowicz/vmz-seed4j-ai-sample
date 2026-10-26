package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import java.time.LocalDateTime;

class PickUpInThePastException extends RentalReservationRuleViolationException {

  PickUpInThePastException(LocalDateTime pickUpAt) {
    super("The pick-up of " + pickUpAt + " is in the past");
  }
}
