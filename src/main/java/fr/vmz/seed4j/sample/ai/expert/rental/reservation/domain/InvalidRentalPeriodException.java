package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import java.time.LocalDateTime;

class InvalidRentalPeriodException extends InvalidRentalReservationValueException {

  InvalidRentalPeriodException(LocalDateTime pickUpAt, LocalDateTime returnAt) {
    super("The return (" + returnAt + ") must be after the pick-up (" + pickUpAt + ")");
  }
}
